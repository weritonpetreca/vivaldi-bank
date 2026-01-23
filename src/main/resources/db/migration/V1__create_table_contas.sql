CREATE TABLE tb_contas (
    id UUID PRIMARY KEY,
    numero_conta VARCHAR(20) NOT NULL UNIQUE,
    saldo NUMERIC(19,2) NOT NULL DEFAULT 0.00,
    titular_nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    criado_em TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_movimentacao (
    id UUID PRIMARY KEY,
    data_hora TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    valor NUMERIC(19,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    conta_id UUID NOT NULL,
    contraparte_nome VARCHAR(255),
    contraparte_numero_conta VARCHAR(20),

    CONSTRAINT fk_movimentacao_conta FOREIGN KEY (conta_id) REFERENCES tb_contas(id)
)