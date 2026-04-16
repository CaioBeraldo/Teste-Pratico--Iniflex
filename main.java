import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final NumberFormat NF = NumberFormat.getInstance(new Locale("pt", "BR"));
    static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    static {
        NF.setMinimumFractionDigits(2);
        NF.setMaximumFractionDigits(2);
    }

    public static void main(String[] args) {

        // 3.1 
        var funcionarios = new ArrayList<>(List.of(
            new Funcionario("Maria",   LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"),  "Operador"),
            new Funcionario("João",    LocalDate.of(1990,  5, 12), new BigDecimal("2284.38"),  "Operador"),
            new Funcionario("Caio",    LocalDate.of(1961,  5,  2), new BigDecimal("9836.14"),  "Coordenador"),
            new Funcionario("Miguel",  LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
            new Funcionario("Alice",   LocalDate.of(1995,  1,  5), new BigDecimal("2234.68"),  "Recepcionista"),
            new Funcionario("Heitor",  LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"),  "Operador"),
            new Funcionario("Arthur",  LocalDate.of(1993,  3, 31), new BigDecimal("4071.84"),  "Contador"),
            new Funcionario("Laura",   LocalDate.of(1994,  7,  8), new BigDecimal("3017.45"),  "Gerente"),
            new Funcionario("Heloísa", LocalDate.of(2003,  5, 24), new BigDecimal("1606.85"),  "Eletricista"),
            new Funcionario("Helena",  LocalDate.of(1996,  9,  2), new BigDecimal("2799.93"),  "Gerente")
        ));

        // 3.2 
        funcionarios.removeIf(f -> f.getNome().equals("João"));
        titulo("3.2 – João removido da lista");

        // 3.3 
        titulo("3.3 – Lista de Funcionários");
        funcionarios.forEach(Principal::imprimirFuncionario);

        // 3.4 
        titulo("3.4 – Salários com 10% de aumento");
        funcionarios.forEach(f -> {
            f.setSalario(f.getSalario().multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP));
            System.out.printf("%-10s | R$ %s%n", f.getNome(), NF.format(f.getSalario()));
        });

        // 3.5 / 3.6 
        titulo("3.6 – Agrupados por Função");
        funcionarios.stream()
            .collect(Collectors.groupingBy(Funcionario::getFuncao))
            .forEach((funcao, lista) -> {
                System.out.println("\nFunção: " + funcao);
                lista.forEach(Principal::imprimirFuncionario);
            });

        // 3.8 
        titulo("3.8 – Aniversariantes em Outubro e Dezembro");
        funcionarios.stream()
            .filter(f -> Set.of(10, 12).contains(f.getDataNascimento().getMonthValue()))
            .forEach(f -> System.out.printf("%-10s | %s%n", f.getNome(), f.getDataNascimento().format(DTF)));

        // 3.9 
        titulo("3.9 – Funcionário com Maior Idade");
        funcionarios.stream()
            .min(Comparator.comparing(Funcionario::getDataNascimento))
            .ifPresent(f -> System.out.printf("%s | %d anos%n",
                f.getNome(), Period.between(f.getDataNascimento(), LocalDate.now()).getYears()));

        // 3.10 
        titulo("3.10 – Ordem Alfabética");
        funcionarios.stream()
            .sorted(Comparator.comparing(Funcionario::getNome))
            .forEach(f -> System.out.printf("%-10s | %s%n", f.getNome(), f.getFuncao()));

        // 3.11 
        titulo("3.11 – Total dos Salários");
        var total = funcionarios.stream()
            .map(Funcionario::getSalario)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total: R$ " + NF.format(total));

        // 3.12 
        titulo("3.12 – Salários Mínimos (SM = R$ 1.212,00)");
        funcionarios.forEach(f -> {
            var qtd = f.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.printf("%-10s | R$ %s | %.2f SM%n",
                f.getNome(), NF.format(f.getSalario()), qtd.doubleValue());
        });
    }

    static void titulo(String texto) {
        System.out.printf("%n=== %s ===%n", texto);
    }

    static void imprimirFuncionario(Funcionario f) {
        System.out.printf("  %-10s | %s | R$ %s | %s%n",
            f.getNome(),
            f.getDataNascimento().format(DTF),
            NF.format(f.getSalario()),
            f.getFuncao());
    }
}