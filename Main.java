import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();
        System.out.println("Calculator Powered By Automata Theory");

		while(true) {
            System.out.print(">>> ");
        	String exp = scanner.nextLine();
            double answer = calculator.eval(exp);
            System.out.println("result: " + answer);
		}
    }
}