package br.unisinos.teoria.computacao.coders;

public enum TipoCodificacao {
    E("Elias-Gamma",1),
    G("Golomb",0),
    F("Fibonacci",2),
    U("Unária",3),
    D("delta",4);

    public static TipoCodificacao parse(int codigo) {
        for(TipoCodificacao codificacao : TipoCodificacao.values()){
            if(codificacao.codigo == codigo){
                return codificacao;
            }
        }
        return null;
    }

    public String getDescricao() {
        return descricao;
    }

    private String descricao;

    private int codigo;

    public int getCodigo() {
        return codigo;
    }

    TipoCodificacao(String descricao, int codigo){
        this.descricao = descricao;
        this.codigo = codigo;
    }


}
