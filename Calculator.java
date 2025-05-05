
public class Calculator {
    private char[] exp;
    private int pointer;


    private void removeWhiteSpaces() {
        int i = 0;

        //skip to space
        while(i < this.exp.length && this.exp[i] != ' ') {
            i++;
        }

        if(i == this.exp.length)
            return;

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
        return parseExp();
    }

    private double parseExp() { 
        //exp = term + term - term + term ... - term
        double result = parseTerm(); 
        while(pointer < exp.length && exp[pointer] != ')') {
            char operator = exp[pointer];

            pointer++;
            switch (operator) {
                case '+':
                    result = result + parseTerm();
                    break;
            
                case '-':
                    result = result - parseTerm();
                    break;

                default: 
				    error();
				    break;
            }

        }

        return result;

    }

    //term = fact*fact/fact*fact...*fact
    private double parseTerm() {
        double result = parseExponent();

        boolean flag = true;
        while (pointer < exp.length && exp[pointer] != ')' && flag) {
            char operator = exp[pointer];

            switch (operator) {
                case '*':
                    pointer++;
                    result = result * parseExponent();
                    break;

                case '/':
                    pointer++;
                    result = result / parseExponent();
                    break;

                case '%':
                    pointer++;
				    result = result % parseExponent();
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

    private double parseExponent() {
        double result = parseFactor();

        if (pointer >= exp.length || exp[pointer] == ')')
            return result;

		char operator = exp[pointer];
        switch (operator) {
			case '^':
            	pointer++;
            	result = Math.pow(result, parseExponent());
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

    //parses numbers, brackets, floating point numbers
    private double parseFactor() {
        double result = 0;

        if(exp[pointer] == '(') {
            pointer++;
            result = parseExp();
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
        } else if(exp[pointer] == '-') {
            pointer++;
            result = -1*parseFactor();
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