CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuario_cpf ON usuarios(cpf);
CREATE INDEX IF NOT EXISTS idx_conta_numero ON contas(numero_conta);
CREATE INDEX IF NOT EXISTS idx_conta_usuario ON contas(usuario_id);
CREATE INDEX IF NOT EXISTS idx_transacao_conta_origem ON transacoes(conta_origem_id);
CREATE INDEX IF NOT EXISTS idx_transacao_conta_destino ON transacoes(conta_destino_id);
CREATE INDEX IF NOT EXISTS idx_transacao_data ON transacoes(data_transacao);
CREATE INDEX IF NOT EXISTS idx_auditoria_entidade ON auditoria(entidade, entidade_id);
CREATE INDEX IF NOT EXISTS idx_auditoria_data ON auditoria(data_acao);

INSERT INTO usuarios (nome, cpf, email, senha, data_criacao, ativo) VALUES
('Administrador Sistema', '00000000000', 'admin@banco.com', '$2a$10$example.hash', NOW(), true),
('João Silva', '12345678901', 'joao.silva@email.com', '$2a$10$example.hash', NOW(), true),
('Maria Santos', '10987654321', 'maria.santos@email.com', '$2a$10$example.hash', NOW(), true)
ON CONFLICT (email) DO NOTHING;

ALTER SYSTEM SET shared_preload_libraries = 'pg_stat_statements';
ALTER SYSTEM SET track_activity_query_size = 2048;
ALTER SYSTEM SET pg_stat_statements.track = 'all';
