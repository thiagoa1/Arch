package br.edu.uni7.arch;

public class Main {

	public class DivisionResult {
		private final int quotient;
		private final int remainder;

		public DivisionResult(int quotient, int remainder) {
			this.quotient = quotient;
			this.remainder = remainder;
		}

		@Override
		public String toString() {
			return "Quociente: " + quotient + " Resto: " + remainder;
		}

		public int getQuotient() {
			return quotient;
		}

		public int getRemainder() {
			return remainder;
		}
	}

	public long multiply(int Q, int M) {
		int A = 0;
		int QMinus1 = 0;
		int minusM = ~M + 1;

		int QQMinus1;
		for (int i = 0; i < 32; i++) {
			QQMinus1 = ((Q & 1) << 1) + QMinus1;
			if ((QQMinus1 & 0b11) == 0b10) {
				// A = sum(A, -M);
				A += minusM;
			} else if ((QQMinus1 & 0b11) == 0b01) {
				// A = sum(A, M);
				A += M;
			}

			QMinus1 = Q & 1;
			Q = (Q >>> 1) /* Não mantém o sinal */ | (A << 31);
			A = A >> 1; // Mantem o sinal
		}

		return (((long) A) << 32) | ((long) Q);
	}

	public DivisionResult divide(int Q, int M) {
		boolean isQNegative = (Q >>> 31) == 1; // Q < 0
		if (isQNegative) {
			Q = ~Q + 1;
		}

		int A = 0;
		int minusM = ~M + 1;

		for (int i = 0; i < 32; i++) {
			A = (A << 1) | ((Q >> 31) & 1);
			Q <<= 1;
			A += minusM;

			boolean isANegative = (A >>> 31) == 1; // A < 0
			if (isANegative) {
				A += M;
			} else {
				Q |= 1;
			}
		}
		if (isQNegative) {
			Q = ~Q + 1;
			A = ~A + 1;
		}
		return new DivisionResult(Q, A);
	}

	public int sum(int a, int b) {
		if (a < 0) {
			a = ~(-a) + 1;
		}
		if (b < 0) {
			b = ~(-b) + 1;
		}
		return a + b;
	}

	public static void main(String[] args) {
		Main main = new Main();

		long multiply1 = main.multiply(9, 5);
		long multiply2 = main.multiply(-9, 5);
		long multiply3 = main.multiply(9, -5);
		
		System.out.println("==== Multiplicações ====");

		System.out.println("9 * 5 = " + multiply1);
		System.out.println("-9 * 5 = " + multiply2);
		System.out.println("9 * -5 = " + multiply3);

		DivisionResult divide1 = main.divide(49, 5);
		DivisionResult divide2 = main.divide(-49, 5);
		DivisionResult divide3 = main.divide(49, -5);
		DivisionResult divide4 = main.divide(-49, -5);
		
		System.out.println("\n==== Divisões ====");

		System.out.println("49 / 5 = " + divide1.quotient + " : 49 % 5 = " + divide1.remainder);
		System.out.println("-49 / 5 = " + divide2.quotient + " : -49 % 5 = " + divide2.remainder);
		System.out.println("49 / -5 = " + divide3.quotient + " : 49 % -5 = " + divide3.remainder);
		System.out.println("-49 / -5 = " + divide4.quotient + " : -49 % -5 = " + divide4.remainder);
	}
}