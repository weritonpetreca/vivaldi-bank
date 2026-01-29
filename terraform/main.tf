terraform {
    required_providers {
        aws = {
            source = "hashicorp/aws"
            version = "~> 5.0"
            }
        }
    }

#Configuração do Provider (A Conexão)
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

resource "aws_security_group" "rds_sg" {
    name = "vivaldi-rds-sg"
    description = "Permite acesso ao RDS Postgres"

    ingress {
        from_port = 5432
        to_port = 5432
        protocol = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
        from_port = 0
        to_port = 0
        protocol = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }
}

resource "aws_ecr_repository" "vivaldi_repo" {
    name = "vivaldi-bank-api"
    image_tag_mutability = "MUTABLE"
    force_delete = true
}

resource "aws_sqs_queue" "transacoes" {
    name = "transacoes-events"
}

resource "aws_sqs_queue" "clientes" {
    name = "clientes-events"
}

resource "aws_sqs_queue" "login" {
    name = "login-events"
}

resource "aws_db_instance" "vivaldi_db" {
    identifier = "vivaldi-db-instance"
    engine = "postgres"
    engine_version = "16"
    instance_class = "db.t3.micro"
    allocated_storage = 20
    username = "admin123"
    password = "admin123"
    db_name = "vivaldi_bank"
    skip_final_snapshot = true
    publicly_accessible = true
    vpc_security_group_ids = [aws_security_group.rds_sg.id]

    deletion_protection = false
}

output "ecr_url" {
    description = "URL do repositório para onde vamos subir a imagem Docker"
    value = aws_ecr_repository.vivaldi_repo.repository_url
}

output "db_endpoint" {
    description = "Endereço de conexão do Banco de Dados (Host:Porta)"
    value = aws_db_instance.vivaldi_db.endpoint
}

output "sqs_filas" {
    description = "URLs das filas criadas para configurar no application.yaml"
    value = {
        transacoes = aws_sqs_queue.transacoes.url
        clientes = aws_sqs_queue.clientes.url
        login = aws_sqs_queue.login.url
    }
}
