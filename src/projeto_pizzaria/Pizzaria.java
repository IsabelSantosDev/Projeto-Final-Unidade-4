package projeto_pizzaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;


public class Pizzaria {
private static final String ENDERECO_PIZZARIA = "Rua da Pizzaria, 100";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Cliente> listaClientes = new ArrayList<>();
        List<Pedido> listaPedidos = new ArrayList<>();

        boolean continuar = true;
        while (continuar) {
            System.out.println();
            System.out.println("Escolha uma opção: ");
            System.out.println("1 - Fazer um novo pedido");
            System.out.println("2 - Alterar um pedido");
            System.out.println("3 - Adicionar um cliente");
            System.out.println("4 - Gerar relatório de vendas");
            System.out.println("5 - Gerar lista de clientes");
            System.out.println("6 - Calcular frete");
            System.out.println("9 - Sair");

            System.out.print("Opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            switch (opcao) {
                case 1:
                    fazerPedido(scanner, listaPedidos, listaClientes);
                    break;
                case 2:
                    Pedido pedido = localizarPedido(scanner, listaPedidos);
                    if (pedido != null) {
                        alterarPizzasPedido(scanner, pedido);
                    }
                    break;
                case 3:
                    listaClientes.add(adicionarCliente(scanner));
                    System.out.println("Cliente adicionado com sucesso!");
                    break;
                case 4:
                    gerarRelatorio(listaPedidos);
                    break;
                case 5:
                    gerarListaClientes(listaClientes);
                    break;
                case 6:
                    calcularFretePedido(scanner, listaPedidos);
                    break;
                case 9:
                    System.out.println("Até mais...");
                    continuar = false;
                    scanner.close();
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
                    break;
            }
        }
    }

    private static void fazerPedido(Scanner scanner, List<Pedido> listaPedidos, List<Cliente> listaClientes) {
        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado! Cadastre um cliente antes de fazer pedidos.");
            return;
        }

        List<Pizza> pizzas = new ArrayList<>();
        System.out.println("FAZER PEDIDO");

        int x = 1;
        System.out.println("Selecione um cliente: ");
        for (Cliente cliente : listaClientes) {
            System.out.println(x + " - " + cliente.getNome());
            x++;
        }
        System.out.print("Opção: ");
        int cliente = scanner.nextInt();
        scanner.nextLine();

        boolean continuar = true;
        while (continuar) {
            x = 1;
            System.out.println("Qual o tamanho da pizza? ");
            System.out.println("Selecione um tamanho: ");
            for (Pizza.TamanhoPizza tamanhos : Pizza.TamanhoPizza.values()) {
                System.out.println(x + " - " + tamanhos);
                x++;
            }
            System.out.print("Opção: ");
            int tamanho = scanner.nextInt();
            scanner.nextLine();

            int quantiSabores = 0;
            while (quantiSabores < 1 || quantiSabores > 4) {
                System.out.println("Digite a quantidade de sabores: 1 - 4 ");
                System.out.print("Opção: ");
                quantiSabores = scanner.nextInt();
                scanner.nextLine();
            }

            Cardapio cardapio = new Cardapio();
            List<String> saboresList = new ArrayList<>();
            List<String> saboresSelect = new ArrayList<>();

            for (int i = 0; i < quantiSabores; i++) {
                System.out.println("Selecione um sabor: ");

                x = 1;
                for (String sabor : cardapio.getCardapio().keySet()) {
                    saboresList.add(sabor);
                    System.out.println(x + " - " + sabor);
                    x++;
                }
                System.out.print("Opção: ");
                int opcao = scanner.nextInt();
                scanner.nextLine();
                saboresSelect.add(saboresList.get(opcao - 1));
            }

            Pizza pizza = new Pizza(
                saboresSelect,
                cardapio.getPrecoJusto(saboresSelect),
                Pizza.TamanhoPizza.getByIndex(tamanho - 1)
            );
            pizzas.add(pizza);

            System.out.println("Pizza adicionada com sucesso!");
            System.out.println();
            System.out.println("Deseja adicionar mais uma pizza no pedido?");
            System.out.print("1 - Sim, 2 - Não: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao != 1) {
                continuar = false;
            }
        }

        Pedido pedido = new Pedido(
    listaPedidos.size() + 1,
    listaClientes.get(cliente - 1),
    listaClientes.get(cliente - 1).getEndereco(),
    pizzas, somarPizzas(pizzas)
);

listaPedidos.add(pedido);
System.out.println("Pedido registrado com sucesso!");
System.out.println("ID do Pedido: " + pedido.getId());
System.out.println("Cliente: " + pedido.getCliente().getNome());
System.out.println("Valor Total: R$ " + pedido.getValorTotal());

}

private static Pedido localizarPedido(Scanner scanner, List<Pedido> listaPedidos) {
    System.out.println("Digite o ID do pedido ou o nome do cliente:");
    String entrada = scanner.nextLine();

    // Primeiro tentar pelo ID
    try {
        int id = Integer.parseInt(entrada);
        for (Pedido pedido : listaPedidos) {
            if (pedido.getId() == id) return pedido;
        }
    } catch (NumberFormatException e) {
        // Se não for número, tentar pelo nome
        for (Pedido pedido : listaPedidos) {
            if (pedido.getCliente().getNome().equalsIgnoreCase(entrada)) return pedido;
        }
    }

    System.out.println("Pedido não encontrado!");
    return null;
}

private static void calcularFretePedido(Scanner scanner, List<Pedido> listaPedidos) {
    if (listaPedidos.isEmpty()) {
        System.out.println("Nenhum pedido registrado!");
        return;
    }

    System.out.print("Digite o ID do pedido: ");
    int id = scanner.nextInt();
    scanner.nextLine();

    Pedido pedidoSelecionado = null;
    for (Pedido p : listaPedidos) {
        if (p.getId() == id) {
            pedidoSelecionado = p;
            break;
        }
    }

    if (pedidoSelecionado == null) {
        System.out.println("Pedido não encontrado!");
        return;
    }

    System.out.print("Digite a distância em km até o endereço de entrega: ");
    double distanciaKm = scanner.nextDouble();
    scanner.nextLine();

    double frete = pedidoSelecionado.calcularFrete(distanciaKm);
    System.out.printf("🚚 Frete do pedido #%d para %s: R$ %.2f%n",
            pedidoSelecionado.getId(),
            pedidoSelecionado.getCliente().getNome(),
            frete);
}


private static void alterarPizzasPedido(Scanner scanner, Pedido pedido) {
        Cardapio cardapio = new Cardapio();
        boolean continuar = true;

        while (continuar) {
            System.out.println("1 - Adicionar pizza");
            System.out.println("2 - Remover pizza");
            System.out.println("3 - Alterar sabor da pizza");
            System.out.println("4 - Sair");
            System.out.print("Opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    // Adicionar pizza
                    List<String> saboresSelect = new ArrayList<>();
                    System.out.println("Quantos sabores? (1-4): ");
                    int quantiSabores = Integer.parseInt(scanner.nextLine());

                    for (int i = 0; i < quantiSabores; i++) {
                        int j = 1;
                        for (String sabor : cardapio.getCardapio().keySet()) {
                            System.out.println(j + " - " + sabor);
                            j++;
                        }
                        System.out.print("Escolha o sabor: ");
                        int escolha = Integer.parseInt(scanner.nextLine());
                        saboresSelect.add((String) cardapio.getCardapio().keySet().toArray()[escolha - 1]);
                    }

                    Pizza novaPizza = new Pizza(saboresSelect, cardapio.getPrecoJusto(saboresSelect), Pizza.TamanhoPizza.MÉDIA);
                    pedido.adicionarPizza(novaPizza);
                    System.out.println("Pizza adicionada!");
                    break;

                case 2:
                    if (pedido.getPizzas().isEmpty()) {
                        System.out.println("Não há pizzas no pedido!");
                        break;
                    }
                    int i = 1;
                    for (Pizza p : pedido.getPizzas()) {
                        System.out.println(i + " - " + p.getSabores());
                        i++;
                    }
                    System.out.print("Escolha a pizza para remover: ");
                    int remover = Integer.parseInt(scanner.nextLine());
                    Pizza pizzaARemover = pedido.getPizzas().get(remover - 1);
                    pedido.removerPizza(pizzaARemover);
                    System.out.println("Pizza removida!");
                    break;

                case 3:
                    if (pedido.getPizzas().isEmpty()) {
                        System.out.println("Não há pizzas para alterar!");
                        break;
                    }
                    i = 1;
                    for (Pizza p : pedido.getPizzas()) {
                        System.out.println(i + " - " + p.getSabores());
                        i++;
                    }
                    System.out.print("Escolha a pizza para alterar: ");
                    int alterar = Integer.parseInt(scanner.nextLine()) - 1;

                    List<String> novosSabores = new ArrayList<>();
                    System.out.println("Quantos sabores? (1-4): ");
                    quantiSabores = Integer.parseInt(scanner.nextLine());
                    for (int j = 0; j < quantiSabores; j++) {
                        int k = 1;
                        for (String sabor : cardapio.getCardapio().keySet()) {
                            System.out.println(k + " - " + sabor);
                            k++;
                        }
                        System.out.print("Escolha o sabor: ");
                        int escolha = Integer.parseInt(scanner.nextLine());
                        novosSabores.add((String) cardapio.getCardapio().keySet().toArray()[escolha - 1]);
                    }
                    pedido.getPizzas().get(alterar).setSabores(novosSabores);
                    System.out.println("Pizza alterada!");
                    break;

                case 4:
                    continuar = false;
                    break;
            }
        }
    }

    private static double somarPizzas(List<Pizza> pizzas) {
        double valorTotal = 0.0;
        for (Pizza pizza : pizzas) {
            valorTotal += pizza.getPreco();
        }
        return valorTotal;
    }

    private static Cliente adicionarCliente(Scanner scanner) {
        System.out.println("Digite o ID do cliente:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Digite o nome do cliente: ");
        String nome = scanner.nextLine();
        System.out.print("Digite o endereço do cliente: ");
        String endereco = scanner.nextLine();
        System.out.print("Digite o telefone do cliente: ");
        String telefone = scanner.nextLine();
        System.out.print("Digite o email do cliente: ");
        String email = scanner.nextLine();

        return new Cliente(id, nome, endereco, telefone, email);
    }

    

    private static void gerarRelatorio(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            return;
        }

        double faturamento = 0;
        Map<String, Integer> contagemSabores = new HashMap<>();

        for (Pedido pedido : pedidos) {
            faturamento += pedido.getValorTotal();
            for (Pizza pizza : pedido.getPizzas()) {
                for (String sabor : pizza.getSabores()) {
                    contagemSabores.put(sabor, contagemSabores.getOrDefault(sabor, 0) + 1);
                }
            }
        }    

        System.out.println("=== RELATÓRIO DE VENDAS ===");
        System.out.println("Faturamento total: R$ " + faturamento);

        System.out.println("\nSabores mais pedidos:");
        contagemSabores.entrySet()
            .stream()
            .sorted((e1, e2) -> e2.getValue() - e1.getValue())
            .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue() + " pedidos"));

        System.out.println("\nConexões entre sabores (grafo):");
        Map<String, List<String>> grafo = gerarGrafoSabores(pedidos);
        for (String s : grafo.keySet()) {
            System.out.println(s + " -> " + grafo.get(s));
        }
    }

    private static Map<String, List<String>> gerarGrafoSabores(List<Pedido> pedidos) {
        Map<String, List<String>> grafo = new HashMap<>();
        for (Pedido pedido : pedidos) {
            for (Pizza pizza : pedido.getPizzas()) {
                List<String> sabores = pizza.getSabores();
                for (String s1 : sabores) {
                    grafo.putIfAbsent(s1, new ArrayList<>());
                    for (String s2 : sabores) {
                        if (!s1.equals(s2) && !grafo.get(s1).contains(s2)) {
                            grafo.get(s1).add(s2);
                        }
                    }
                }
            }
        }
        return grafo;
    }

    private static void gerarListaClientes(List<Cliente> listaClientes) {
        if (listaClientes.isEmpty()) {
            System.out.println("Lista de clientes está vazia.");
        } else {
            int x = 1;
            for (Cliente cliente : listaClientes) {
                System.out.println("Cliente " + x);
                System.out.println("Nome: " + cliente.getNome());
                System.out.println("Endereço: " + cliente.getEndereco());
                System.out.println("Telefone: " + cliente.getTelefone());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("----------------------");
                x++;
            }
        }
    }
}

