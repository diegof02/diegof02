import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AFD {

	private LinkedList<String> symbols;
	private LinkedList<Integer> finalStates;
	HashMap<Pair<Integer, String>, Integer> transitions;
	private int stateCount;

	public AFD(String path) {

		File file;
		Scanner reader;
		
		symbols = new LinkedList<String>();
		finalStates = new LinkedList<Integer>();
		transitions = new HashMap<Pair<Integer, String>, Integer>();
		stateCount = 0;

		try {
			file = new File(path); 
			reader = new Scanner(file);

			if (reader.hasNextLine()) {
				String chars = reader.nextLine();
				Scanner charsReader = new Scanner(chars);
				charsReader.useDelimiter(",");
				
				while (charsReader.hasNext()) {
					symbols.add(charsReader.next());
				}
				charsReader.close();
			}

			if (reader.hasNextLine()) {
				stateCount = reader.nextInt();
				reader.nextLine();
			}

			if (reader.hasNextLine()) {
				String states = reader.nextLine();
				Scanner statesReader = new Scanner(states);

				statesReader.useDelimiter(",");
				while (statesReader.hasNext()) {
					int n = statesReader.nextInt();
					finalStates.add(n);
				}

				statesReader.close();
			}

			transitions = new HashMap<Pair<Integer, String>, Integer>();
	
			int symbolIndex = 0;
			while (reader.hasNextLine()) {

				String row = reader.nextLine();
				Scanner transitionsReader = new Scanner(row);				
				transitionsReader.useDelimiter(",");
		
				int state = 0;
				while (transitionsReader.hasNext() && symbolIndex < symbols.size()) {
					
					Pair<Integer, String> current_transition = new Pair<>(state, symbols.get(symbolIndex));
					transitions.put(current_transition, transitionsReader.nextInt()); 					

					state++;					

				}
				
				transitionsReader.close();
				symbolIndex++;

			}

			reader.close();

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("File not found.");
		}

	}


	public int getTransition(int currentState, char symbol) {

		for (Pair<Integer, String> p : transitions.keySet())
		{
			if (p.first() == currentState && p.second().equals(symbol + "")) {
				return transitions.get(p);
			}  
		}
		
		return 0;

	}


	private int delta(int state, String string) {

		if (string.length() == 0)
			return state;

		return delta(getTransition(state, string.charAt(0)), string.substring(1, string.length()));

	}

	public boolean accept(String string) {
		
		int res = delta(1, string);
		for (int i = 0; i < finalStates.size(); i++)
			if (res == finalStates.get(i)) return true;

		return false;

	}


	public static void main(String[] args) throws Exception {
	
		try {

			String nameFile = args[0].replace("/", "\\");
			String flag = args[1];
			AFD afd = new AFD(nameFile);
	
			switch(flag) {
				case "-f":
					System.out.println("-------MODO BATCH------");
					String textFilePath = args[2].replace("/", "\\");
	
					try {
						File textFile = new File(textFilePath);
						Scanner reader = new Scanner(textFile);
	
						while (reader.hasNextLine()) {
							
							String str = reader.nextLine();
							boolean isAccepted = afd.accept(str);
							System.out.println(str + ": " + (isAccepted ? "Aceptada" : "Rechazada"));	
	
						}
					}
					catch (FileNotFoundException e) {
						System.err.println("File not found.");
					}
	
					break;
				case "-i":
					System.out.println("-------MODO INTERACTIVO------");

					String str = "";
					Scanner sc = new Scanner(System.in);

					while (true) {

						System.out.print("> ");
						
						str = sc.nextLine();
						if (str.length() < 1) 
							break;

						System.out.println(afd.accept(str) ? "Aceptada" : "Rechazada");

					}

					break;

				default:
					System.out.println("Error! Invalid argument");
					break;
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Enter all arguments");
		}
	}
}

class Pair <T, U> {

	private T _first;
	private U _second; 

	public Pair(T first, U second) {

		_first = first;
		_second = second;

	}

	public T first() {
	
		return _first;

	} 	

	public U second() {

		return _second;
	
	}

	@Override
	public int hashCode() {
		return _first.hashCode() + _second.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		
		if (o instanceof Pair) {

			return (((Pair)o)._first == _first) && (((Pair)o)._second == _second);

		}

		return false;

	}

}
