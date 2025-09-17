package projeto_pizzaria;

import java.util.List;
import java.util.ArrayList;

public class Pizza {
    private List<String> sabores;
    private double preco;
    private TamanhoPizza tamanho;

    public enum TamanhoPizza {
        BROTO,
        MÉDIA,
        GRANDE;

        public static TamanhoPizza getByIndex(int index) {
            TamanhoPizza[] tamanhos = TamanhoPizza.values();
            if (index >= 0 && index < tamanhos.length) {
                return tamanhos[index];
            } else {
                throw new IllegalArgumentException("Posição incorreta do index");
            }
        }
    }

    public Pizza(List<String> sabores, double preco, TamanhoPizza tamanho) {
        this.sabores = new ArrayList<>(sabores);
        this.preco = preco;
        this.tamanho = tamanho;
    }

    public List<String> getSabores() {
        return sabores;
    }

    public double getPreco() {
        return preco;
    }

    public TamanhoPizza getTamanho() {
        return tamanho;
    }

    public void setSabores(List<String> sabores) {
        this.sabores = new ArrayList<>(sabores);
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void setTamanho(TamanhoPizza tamanho) {
        this.tamanho = tamanho;
    }


    // Adicionar mais um sabor à pizza
    public void adicionarSabor(String sabor) {
        if (!sabores.contains(sabor)) {
            sabores.add(sabor);
        }
    }

    // Remover um sabor da pizza
    public void removerSabor(String sabor) {
        sabores.remove(sabor);
    }

    // Exibir todos os detalhes da pizza formatados
    public void mostrarDetalhesPizza() {
        System.out.println("🍕 Pizza:");
        System.out.println(" - Tamanho: " + tamanho);
        System.out.println(" - Sabores: " + String.join(", ", sabores));
        System.out.println(" - Preço: R$ " + preco);
    }

    // Clonar uma pizza (útil se o cliente pede a mesma mais de uma vez)
    public Pizza clonar() {
        return new Pizza(new ArrayList<>(sabores), preco, tamanho);
    }
}
