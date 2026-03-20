terraform {
    required_providers {
        aws = {
            source  = "hashicorp/aws"
            version = "~> 5.0"
        }
        http = {
            source  = "hashicorp/http"
            version = "~> 3.0"
        }
    }
}

provider "aws" {
    region  = "us-east-1"
    profile = "default"

    default_tags {
        tags = {
            Project     = "VivaldiBank"
            Owner       = "Weriton"
            Environment = "Dev"
        }
    }
}

# ==============================================================================
# VARIÁVEIS
# ==============================================================================

variable "db_password" {
    description = "Senha do banco de dados RDS PostgreSQL"
    type        = string
    sensitive   = true
}

# ==============================================================================
# IP DINÂMICO
# Detecta automaticamente o IP público da máquina que executa o apply.
# O Security Group liberará apenas esse IP para acessar o RDS.
# ==============================================================================

data "http" "my_ip" {
    url = "https://ifconfig.me/ip"
}

locals {
    my_cidr = "${trimspace(data.http.my_ip.response_body)}/32"
}

# ==============================================================================
# REDE
# ==============================================================================

data "aws_vpc" "default" {
    default = true
}

data "aws_subnets" "default" {
    filter {
        name   = "vpc-id"
        values = [data.aws_vpc.default.id]
    }
}

resource "aws_db_subnet_group" "vivaldi" {
    name       = "vivaldi-db-subnet-group"
    subnet_ids = data.aws_subnets.default.ids

    tags = {
        Name = "vivaldi-db-subnet-group"
    }
}

# ==============================================================================
# SECURITY GROUPS
# ==============================================================================

resource "aws_security_group" "rds_sg" {
    name        = "vivaldi-rds-sg"
    description = "Permite acesso ao RDS Postgres apenas pela aplicacao"
    vpc_id      = data.aws_vpc.default.id

    ingress {
        from_port   = 5432
        to_port     = 5432
        protocol    = "tcp"
        cidr_blocks = [local.my_cidr]
        description = "PostgreSQL - IP detectado automaticamente no apply"
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
        description = "Permite saida"
    }

    tags = {
        Name = "vivaldi-rds-sg"
    }
}

# ==============================================================================
# ECR
# ==============================================================================

resource "aws_ecr_repository" "vivaldi_repo" {
    name                 = "vivaldi-bank-api"
    image_tag_mutability = "MUTABLE"
    force_delete         = true

    image_scanning_configuration {
        scan_on_push = true
    }
}

resource "aws_ecr_lifecycle_policy" "vivaldi_policy" {
    repository = aws_ecr_repository.vivaldi_repo.name

    policy = jsonencode({
        rules = [{
            rulePriority = 1
            description  = "Manter apenas as 3 ultimas imagens"
            selection = {
                tagStatus   = "any"
                countType   = "imageCountMoreThan"
                countNumber = 3
            }
            action = { type = "expire" }
        }]
    })
}

# ==============================================================================
# SQS
# ==============================================================================

resource "aws_sqs_queue" "transacoes" {
    name = "transacoes-events"
}

resource "aws_sqs_queue" "clientes" {
    name = "clientes-events"
}

resource "aws_sqs_queue" "login" {
    name = "login-events"
}

# ==============================================================================
# RDS
# ==============================================================================

resource "aws_db_instance" "vivaldi_db" {
    identifier     = "vivaldi-db-instance"
    engine         = "postgres"
    engine_version = "16"
    instance_class = "db.t3.micro"

    allocated_storage = 20
    db_name           = "vivaldi_bank"
    username          = "admin123"
    password          = var.db_password

    db_subnet_group_name   = aws_db_subnet_group.vivaldi.name
    vpc_security_group_ids = [aws_security_group.rds_sg.id]

    # Acessível publicamente para desenvolvimento local.
    # O SG restringe o acesso apenas ao IP detectado no apply.
    publicly_accessible = true

    # Proteção contra delete acidental.
    # Para destruir: primeiro desative via AWS CLI:
    # aws rds modify-db-instance --db-instance-identifier vivaldi-db-instance \
    #   --no-deletion-protection --apply-immediately
    deletion_protection = true

    skip_final_snapshot = true
}

# ==============================================================================
# OUTPUTS
# ==============================================================================

output "ecr_url" {
    description = "URL do repositorio ECR para push da imagem Docker"
    value       = aws_ecr_repository.vivaldi_repo.repository_url
}

output "db_endpoint" {
    description = "Endpoint de conexao do RDS (host:porta)"
    value       = aws_db_instance.vivaldi_db.endpoint
}

output "sqs_filas" {
    description = "URLs das filas SQS para configurar no application.yaml"
    value = {
        transacoes = aws_sqs_queue.transacoes.url
        clientes   = aws_sqs_queue.clientes.url
        login      = aws_sqs_queue.login.url
    }
}

output "ip_liberado" {
    description = "IP que foi liberado no Security Group do RDS"
    value       = local.my_cidr
}
