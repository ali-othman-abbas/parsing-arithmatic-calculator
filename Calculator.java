import java.util.Scanner;

public class Calculator {
    private char[] exp;
    private int pointer;


    private void removeWhiteSpaces() {
        int i = 0;

        while(i < this.exp.length && this.exp[i] != ' ') {
            i++;
        }

        int j = i + 1;

        while(j < this.exp.length) {
            if(this.exp[j] != ' ') {
                this.exp[i] = this.exp[j];
                i++;
            }
            j++;
        }

        char[] temp = new char[i];
        System.arraycopy(this.exp, 0, temp, 0, i);
        this.exp = temp;
    }

    public double eval(String exp) {
        this.exp = exp.toCharArray();
        this.pointer = 0;
        this.removeWhiteSpaces();
        return parseS();
    }

    private double parseS() { 
        double result = parseA();
        while(pointer < exp.length && exp[pointer] != ')') {
            char operator = exp[pointer];

            pointer++;
            switch (operator) {
                case '+':
                    result = result + parseA();
                    break;
            
                case '-':
                    result = result - parseA();
                    break;

                default: 
				    error();
				    break;
            }

        }

        return result;

    }

    private double parseA() {
        double result = parseT();

        boolean flag = true;
        while (pointer < exp.length && exp[pointer] != ')' && flag) {
            char operator = exp[pointer];

            switch (operator) {
                case '*':
                    pointer++;
                    result = result * parseT();
                    break;

                case '/':
                    pointer++;
                    result = result / parseT();
                    break;

                case '%':
                    pointer++;
				    result = result % parseT();
				    break;

			    case '+':
			    case '-':
                    flag = false;
				    break;
        
                default:
                    flag = false;
				    break;
		    }

        }
        return result;

    }

    private double parseT() {
        double result = parseM();

        if (pointer >= exp.length || exp[pointer] == ')')
            return result;

		char operator = exp[pointer];
        switch (operator) {
			case '^':
            	pointer++;
            	result = Math.pow(result, parseT());
				break;
            case '*':
            case '/':
			case '%':
			case '+':
			case '-':
				break;
        
            default:
				break;
		}
		return result;
    }

    private double parseM() {
        double result = 0;
        if(exp[pointer] == '(') {
            pointer++;
            result = parseS();
			if(pointer >= exp.length) {
				error();
				return result;
			}
            pointer++;
            return result;
        } else if(exp[pointer] >= '0' && exp[pointer] <= '9') {
            result = parseI();
            return result;
		} else if(
				pointer + 1 < exp.length && 
				exp[pointer] == '.' &&
				isDigit(exp[pointer + 1])
				) {
			pointer++;
			result = parseF();
			return result;
        } else {
			return result;
		}
    }

    private double parseI() {
        double num = 0;
        while(pointer < exp.length && isDigit(exp[pointer])) {
            num = 10*num + (exp[pointer] - '0');
            pointer++;
        }

		double decimal = 0;
		if(
				pointer + 1 < exp.length 
				&& exp[pointer] == '.'
				&& isDigit(exp[pointer + 1])) {
			pointer++;
			decimal = parseF();
		}

		num = num + decimal; 
		return num;
    }

	private double parseF() {
		double decimal = 0;
		double pow = 1;

		while( pointer < exp.length && isDigit( exp[pointer] ) )  {
			decimal = 10*decimal + ( exp[pointer] - '0' );
			pow = pow * (1.0/10);
			pointer++;
		}


		decimal = decimal * pow;

		return decimal;
	}

	private boolean isDigit(char character) {
		return character >= '0' && character  <= '9';
	}
    

	public void error() {
		System.out.println("Syntax Error");
		System.exit(1);
	}

}
