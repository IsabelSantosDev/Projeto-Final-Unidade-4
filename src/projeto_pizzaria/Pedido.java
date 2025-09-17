package projeto_pizzaria;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<Pizza> pizzas;
    private double valorTotal;
    private String enderecoEntrega;

    public Pedido(int id, Cliente cliente, String enderecoEntrega, List<Pizza> pizzas, double valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.pizzas = (pizzas != null) ? pizzas : new ArrayList<>();
        this.valorTotal = valorTotal;
        this.enderecoEntrega = enderecoEntrega;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public void adicionarPizza(Pizza pizza) {
        pizzas.add(pizza);
        calcularValorTotal();
    }

    public void removerPizza(Pizza pizza) {
        pizzas.remove(pizza);
        calcularValorTotal();
    }

    private void calcularValorTotal() {
        valorTotal = valorTotal;
        for (Pizza pizza : pizzas) {
            valorTotal += pizza.getPreco();
        }
    }


    // Alterar um pedido existente (sabores, pizzas, etc.)
    public void alterarPizza(int indexPizza, List<String> novosSabores, Double novoPreco, Pizza.TamanhoPizza novoTamanho) {
        if (indexPizza >= 0 && indexPizza < pizzas.size()) {
            Pizza pizza = pizzas.get(indexPizza);

            if (novosSabores != null && !novosSabores.isEmpty()) {
                pizza.setSabores(novosSabores);
            }
            if (novoPreco != null && novoPreco > 0) {
                pizza.setPreco(novoPreco);
            }
            if (novoTamanho != null) {
                pizza.setTamanho(novoTamanho);
            }

            calcularValorTotal();
        } else {
            System.out.println("⚠️ Pizza não encontrada no pedido!");
        }
    }

    // Exibir detalhes do pedido formatados
    public void mostrarDetalhesPedido() {
        System.out.println("📦 Pedido ID: " + id);
        System.out.println("👤 Cliente: " + cliente.getNome());
        System.out.println("🏠 Endereço: " + enderecoEntrega);
        System.out.println("🍕 Pizzas:");
        for (int i = 0; i < pizzas.size(); i++) {
            System.out.println("   #" + (i + 1));
            pizzas.get(i).mostrarDetalhesPizza();
        }
        System.out.println("💰 Valor Total: R$ " + valorTotal);
    }

    // Cálculo do frete (peso = número de pizzas * 0.5kg por pizza, taxa fixa por km)
    public double calcularFrete(double distanciaKm) {
        double peso = pizzas.size() * 0.5; // cada pizza = 0.5kg (exemplo simplificado)
        double taxaPorKm = 2.0; // R$2,00 por km
        double taxaPorKg = 1.5; // R$1,50 por kg
        return (distanciaKm * taxaPorKm) + (peso * taxaPorKg);
    }

    @Override
    public String toString() {
        return "Pedido ID: " + id +
                "\nCliente: " + cliente.getNome() +
                "\nEndereço de entrega: " + enderecoEntrega +
                "\nValor Total: R$ " + valorTotal +
                "\nQuantidade de Pizzas: " + pizzas.size();
    }
}
