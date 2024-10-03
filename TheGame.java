import java.util.*;
import java.io.*;

public class TheGame {

	private static int gamerounds = 1;

	private static ArrayList<Pokemon> pokemons;
	private static ArrayList<Pokemon> playersPokemons;
	private static ArrayList<Pokemon> enemyPokemons;
	private static String onStage = "";
	private static int index = -1;
	private static Boolean bigPlayerSkip = false;
	private static Boolean bigEnemySkip = false;
	private static ArrayList<String> playerPokeballStash = new ArrayList<>();
	static int playerHighScore = 0;




	public static String inputTrainerName() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your 3-character trainer name:");

		while (true) {
			String input = scanner.nextLine().trim();

			if (input.length() == 3) {
				return input;
			} else {
				System.out.println("Invalid input. Please enter a 3-character name:");
			}
		}
	}

	public static void writeToHighScoresFile(String trainerName) {
		try (FileWriter writer = new FileWriter("highscores.txt", true);
			 BufferedWriter bufferedWriter = new BufferedWriter(writer);
			 PrintWriter out = new PrintWriter(bufferedWriter)) {

			out.println(trainerName);

		} catch (IOException e) {
			System.err.println("Error writing to the highscores file: " + e.getMessage());
		}
	}



	public static int calculateHighScore(int gamerounds,ArrayList<String> playerPokeballStash) {
		int baseHighScore = 0;
		int highScore = (baseHighScore  + ((gamerounds+1) * 100))*(playerPokeballStash.size()+1);




		return highScore;
	}





	public static void mainmenu() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your choice:");
		System.out.println("1. start the game");
		System.out.println("2. highscore list");
		System.out.println("3. exit");
		int choice = scanner.nextInt();


		switch (choice) {
			case 1:
				Game();
				break;
			case 2:
				System.out.println("Showing highscore list...");
				printFileContent("highscores.txt");
				mainmenu();
			case 3:
				break;
			default:
				System.out.println("Invalid choice. Please enter 1 or 2.");
		}
	}


	public static void Game() {
		pokemons = new ArrayList<Pokemon>();
		playersPokemons = new ArrayList<Pokemon>();
		enemyPokemons = new ArrayList<Pokemon>();

		welcomeMessage();
		readFile();
		pokemonSelection();
		enemyCreator();
		giveRandomPokeball();

		Collections.shuffle(enemyPokemons);

		int round = 1;
		int gameround = 1;
		int defeatedEnemies = 0;


		if (firstTurn() == 1) {
			System.out.println("Player Starts");
			while (validGame() && gameround <= 24) {
				if (round == 1) {
					choosePokes();
				}
				chooseEnemy();
				playerMove();
				calculateDamage();
				if (validGame() && bigEnemySkip == false) {
					enemyMove();
					calculateDamage();
				}
				bigEnemySkip = false;
				addEnergy();
				round++;

				if (round > playersPokemons.size() && round > enemyPokemons.size()) {
					round = 1;
					gameround++;
				}



				playerHighScore = calculateHighScore(gamerounds, playerPokeballStash);
			}

			checkWinner();
			System.out.println("Player's High Score: " + playerHighScore);
		}
		else {
			System.out.println("Enemy Starts");
			while (validGame() && gameround <= 24) {
				if (round == 1) {
					choosePokes();
				}
				chooseEnemy();
				enemyMove();
				calculateDamage();
				if (validGame() && bigPlayerSkip == false) {
					playerMove();
					calculateDamage();
				}
				bigPlayerSkip = false;
				addEnergy();
				round++;

				if (round > playersPokemons.size() && round > enemyPokemons.size()) {
					round = 1;
					gameround++;
				}


				playerHighScore = calculateHighScore( gamerounds, playerPokeballStash);
			}
		}

		checkWinner();
		System.out.println("Player's High Score: " +playerHighScore );
	}


	public static void welcomeMessage() {

		try {
			Scanner inFile = new Scanner((new BufferedReader(new FileReader("Icon.txt"))));
			while (inFile.hasNextLine()) {
				System.out.println(inFile.nextLine());
			}
		}

		catch (IOException ex) {

		}
		System.out.print("\n");
		System.out.print(" Welcome to the Pokemon Gaole! \n" +
				" Bring your team of 1-6 Pokemon and face off against\n" +
				"a MASSIVE lineup of enemy Pokemon. Will you win?\n" +
				"o Good Luck! \n");
		System.out.println("Press Enter to Start");
		Scanner kb = new Scanner(System.in);
		String nothing = kb.nextLine();
		printDivider();
	}

	public static void printDivider() {
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
	}

	public static void readFile() {
		String fileName = "pokemon.txt";
		Scanner kb = new Scanner(System.in);

		try {
			Scanner inFile = new Scanner(new File(fileName));
			int n = inFile.nextInt();
			inFile.nextLine();
			for (int i = 0; i < n; i++) {
				Pokemon temp = new Pokemon(inFile.nextLine());
				pokemons.add(temp);
			}
		}
		catch (IOException ex) {
			System.out.println(" misplace pokemon.txt?");

		}
	}

	public static void pokemonSelection() {
		Scanner kb = new Scanner(System.in);
		printPokemonInfo();
		while (true) {
			printDivider();
			int choice = 0;
			if (playersPokemons.size() < 2) {
				System.out.println("Enter Pokemon #" + (playersPokemons.size() + 1));
				choice = kb.nextInt();
			}
			else {
				break;
			}
			if (choice < pokemons.size() && choice >= 0 && !(playersPokemons.contains(pokemons.get(choice)))) {
				playersPokemons.add(pokemons.get(choice));
				System.out.println("You selected " + pokemons.get(choice).getName() +
						"| Type: " + pokemons.get(choice).getType().toUpperCase());
			}
			else {
				System.out.println("Invalid Input, Try Again");
			}
		}
	}

	public static void enemyCreator() {
		for (int i = 0; i < pokemons.size(); i++) {
			if (!(playersPokemons.contains(pokemons.get(i)))) {
				enemyPokemons.add(pokemons.get(i));
			}

		}
		printDivider();
	}

	public static void printPlayerInfo() {
		for (int i = 0; i < playersPokemons.size(); i++) {
			Pokemon temp = playersPokemons.get(i);
			System.out.println(i + ". " + temp.getName() + "| Type: " + temp.getType().toUpperCase() +
					"| Health: " + temp.getHealth() + "| Energy: " + temp.getEnergy());
		}
	}

	public static void printPokemonInfo() {
		for (int i = 0; i < pokemons.size(); i++) {
			Pokemon temp = pokemons.get(i);
			System.out.println(i + ". " + temp.getName() + "| Type: " + temp.getType().toUpperCase() +
					"| Health: " + temp.getHealth() + "| Energy: " + temp.getEnergy());
		}
	}

	public static int firstTurn() {
		Random rand = new Random();
		int n = rand.nextInt(2) + 1;
		return n;
	}

	public static Boolean validGame() {
		if (playersPokemons.size() == 0) {
			return false;
		}
		if (enemyPokemons.size() == 0) {
			return false;
		}
		return true;
	}

	public static void choosePokes() {
		System.out.println("You have " + playersPokemons.size() + " Pokemon left");
		printPlayerInfo();
		System.out.println("Which Pokemon would you like to use for battle?");

		while (true) {
			Scanner kb = new Scanner(System.in);
			int n = kb.nextInt();
			if (n >= 0 && n < playersPokemons.size()) {
				onStage = playersPokemons.get(n).getName();
				index = n;
				System.out.println(onStage + ", I choose you!");
				break;
			}
			else {
				System.out.println("Invalid Input");
			}

			printDivider();
		}

	}

	public static void chooseEnemy() {
		System.out.println("You are battling " + enemyPokemons.get(0).getName() +
				"| Type: " + enemyPokemons.get(0).getType().toUpperCase());
		printDivider();
	}

	public static void playerMove() {
		Scanner kb = new Scanner(System.in);
		System.out.println(onStage + " is currently on the field. What will Trainer do?");

		System.out.println("1: Attack");
		System.out.println("2: Switch Pokemon");
		System.out.println("3: Pass/Skip");
		System.out.println("4: Use Pokeball");
		System.out.println("5: Highscores");
		int choice = 0;
		while (true) {
			choice = kb.nextInt();
			if (choice == 1 && playersPokemons.get(index).getStuned() == true) {
				System.out.println("Invalid, this pokemon is stunned");
			}
			else if (choice > 0 && choice <= 5) {
				break;
			}
			else {
				System.out.println("Invalid Input.");
			}
		}
		printDivider();
		if (choice == 1) {
			choseAttack();
		}
		else if (choice == 2) {
			choseSwitch();
		}
		else if (choice == 3) {
			chosePass();
		}
		else if (choice == 4) {
			chosePokeball();
		}

		else {
			printFileContent("highscores.txt");
		}

	}

	public static void choseAttack() {
		Scanner kb = new Scanner(System.in);
		for (int i = 0; i < playersPokemons.size(); i++) {
			if (playersPokemons.get(i).getName() == onStage) {
				index = i;
			}
		}
		System.out.println("Trainer wants to Attack! Your options are:");

		for (int i = 0; i < playersPokemons.get(index).getNumAttacks(); i++) {
			String[] temp = playersPokemons.get(index).getMove(i);
			String special;
			if (temp[3].equals(" ")) {
				special = ("No special");
			}
			else {
				special = temp[3];
			}
			System.out.println(i + ": " + temp[0] + "| Special: " + special);
		}
		System.out.println(playersPokemons.get(index).getName() + " has " + playersPokemons.get(index).getEnergy() + " energy left.");
		while (true) {
			int attackChoice = kb.nextInt();
			if (attackChoice < 0 || attackChoice > playersPokemons.get(index).getNumAttacks()) {
				System.out.println("Invalid Input");
			} else if (playersPokemons.get(index).getEnergy() < playersPokemons.get(index).getMoveCost(attackChoice)) {
				System.out.println("Not enough energy.");
				printDivider();
				playerMove();
			}
			else {
				System.out.println(playersPokemons.get(index).getName() + " used "
						+ playersPokemons.get(index).getMoveName(attackChoice) + "!");
				int temp = playersPokemons.get(index).getEnergy();
				playersPokemons.get(index).setEnergy(temp - playersPokemons.get(index).getMoveCost(attackChoice));
				useAttack(playersPokemons.get(index).getMove(attackChoice), "enemy");
				break;
			}
		}
		printDivider();
	}

	public static void useAttack(String[] move, String target) {
		Boolean wildCard = false;
		Boolean wildStorm = false;
		Boolean skip = false;
		Boolean enemyWildCard = false;
		Boolean enemyWildStorm = false;
		Boolean enemySkip = false;
		if (target == "enemy" && playersPokemons.get(index).getStuned() == false) {
			String special = move[3];
			if (!(special.equals(" "))) {
				Random rand = new Random();
				int rando = rand.nextInt(2) + 0;
				if (rando == 1 && special.equals("stun")) {
					System.out.println(playersPokemons.get(index).getName() + " landed a stun!");
					enemyPokemons.get(0).setStuned(true);
				}
				else if (rando == 1 && special.equals("wild card")) {
					System.out.println("Wild Card! Attack Failed");
					wildCard = true;
				}
				else if (rando == 1 && special.equals("wild storm")) {
					wildStorm = true;
					skip = true;
					rawPlayerAttack(move);
					while (wildStorm) {
						Random randInt = new Random();
						int nextRand = randInt.nextInt(2) + 0;
						if (nextRand == 1) {
							System.out.println("Wild Storm!");
							rawPlayerAttack(move);
						}
						else {
							System.out.println("Wild Storm Over!");
							wildStorm = false;
						}
					}
				}

				else if (rando == 1 && special.equals("disable")) {
					System.out.println(playersPokemons.get(index).getName() + " landed a DISABLE!");
					enemyPokemons.get(0).setDisable();
				}

				else if (rando == 1 && special.equals("recharge")) {
					System.out.println(playersPokemons.get(index).getName() + "Got an Energy Recharge!");
					int currEnergy = playersPokemons.get(index).getEnergy();
					if (currEnergy < 30) {
						playersPokemons.get(index).setEnergy(currEnergy + 20);
					}
					else {
						playersPokemons.get(index).setEnergy(50);
					}
				}
			}
		}

		else if (target == "player" && enemyPokemons.get(0).getStuned() == false) {
			String special = move[3];
			if (!(special.equals(" "))) {
				Random rand = new Random();
				int rando = rand.nextInt(2) + 0;
				if (rando == 1 && special.equals("stun")) {
					System.out.println(enemyPokemons.get(0).getName() + " landed a stun!");
					playersPokemons.get(index).setStuned(true);
				}
				else if (rando == 1 && special.equals("wild card")) {
					System.out.println("Wild Card! Attack Failed");
					enemyWildCard = true;
				}
				else if (rando == 1 && special.equals("wild storm")) {
					enemyWildStorm = true;
					enemySkip = true;
					rawEnemyAttack(move);
					while (enemyWildStorm) {
						Random randInt = new Random();
						int nextRand = randInt.nextInt(2) + 0;
						if (nextRand == 1) {
							System.out.println("Enemy Wild Storm!");
							rawEnemyAttack(move);
						}
						else {
							System.out.println("Enemy Wild Storm Over!");
							enemyWildStorm = false;
						}
					}
				}

				else if (rando == 1 && special.equals("disable")) {
					System.out.println(enemyPokemons.get(0).getName() + " landed a DISABLE!");
					playersPokemons.get(index).setDisable();
				}

				else if (rando == 1 && special.equals("recharge")) {
					int currEnergy = enemyPokemons.get(0).getEnergy();
					System.out.println(enemyPokemons.get(0).getName() + "Got an Energy Recharge!");
					if (currEnergy < 30) {
						enemyPokemons.get(0).setEnergy(currEnergy + 20);
					}
					else {
						enemyPokemons.get(0).setEnergy(50);
					}
				}
			}
		}
		if (target == "enemy" && wildCard == false && skip == false) {
			rawPlayerAttack(move);
		}
		else if (target == "player" && enemyWildCard == false && enemySkip == false) {
			rawEnemyAttack(move);
		}
		wildCard = false;
		skip = false;
		enemyWildCard = false;
		enemySkip = false;
		printDivider();
	}

	public static void calculateDamage() {
		if (playersPokemons.get(index).getHealth() <= 0) {
			System.out.println(playersPokemons.get(index).getName() + " has fainted! Chose another pokemon for battle.");
			playersPokemons.remove(index);
			for (int i = 0; i < playersPokemons.size(); i++) {
				System.out.println(i + ". " + playersPokemons.get(i).getName());
			}
			bigPlayerSkip = true;
			if (validGame()) {
				while (true) {
					Scanner kb = new Scanner(System.in);
					System.out.println("Enter your choice of move:");
					int temp = kb.nextInt();
					if (temp >= 0 && temp < playersPokemons.size()) {
						onStage = playersPokemons.get(index).getName();
						System.out.println("Trainer Selected " + onStage + " |Type :" + playersPokemons.get(index).getType().toUpperCase());
						break;
					}
					else {
						System.out.println("Invalid Input");
					}
				}
			}
		}
		else {
			System.out.println(playersPokemons.get(index).getName() + " has " + playersPokemons.get(index).getHealth() + " health left");
		}

		if (enemyPokemons.get(0).getHealth() <= 0) {
			bigEnemySkip = true;
			gamerounds++;

			System.out.println(gamerounds);
			System.out.println(enemyPokemons.get(0).getName() + " has fainted!  Pokemon in your party is having a health bonus.");

			giveRandomPokeball();

			checkWinner();

			enemyPokemons.remove(0);

			if (validGame()) {
				System.out.println(enemyPokemons.get(0).getName() + " is entering the field!");
			}
			for (int i = 0; i < playersPokemons.size(); i++) {
				int currHealth = playersPokemons.get(i).getHealth();
				playersPokemons.get(i).setEnergy(50);
				if (currHealth > playersPokemons.get(i).getMaxHealth() - 20) {
					playersPokemons.get(i).setHealth(playersPokemons.get(i).getMaxHealth());
				}
				else {
					playersPokemons.get(i).setHealth(currHealth + 20);
				}
			}
		}
		else {
			System.out.println(enemyPokemons.get(0).getName() + " has " + enemyPokemons.get(0).getHealth() + " health left");
		}
		printDivider();
	}
	public static void choseSwitch() {
		Scanner kb = new Scanner(System.in);
		System.out.println("Which pokemon would you like to switch fpr battle?");
		printPlayerInfo();
		int switched = 0;
		while (true) {
			switched = kb.nextInt();
			if (switched >= 0 && switched < playersPokemons.size()) {
				break;
			}
			else {
				System.out.println("Invalid Input.");
			}
		}
		onStage = playersPokemons.get(switched).getName();
		index = switched;
		printDivider();
	}

	public static void chosePass() {
		System.out.println("Trainer Chose to Passed!");
		playersPokemons.get(index).setStuned(false);
		printDivider();
	}
	public static void printFileContent(String fileName) {
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("Error reading the file: " + e.getMessage());
		}
		printDivider();
	}


	public static void chosePokeball() {
		if (playerPokeballStash.isEmpty()) {
			System.out.println("You don't have any Pokeball! Win battle to get it.");
			printDivider();
			return;
		}

		System.out.println("Your Pokeball Stash:");
		for (int i = 0; i < playerPokeballStash.size(); i++) {
			System.out.println(i + ". " + playerPokeballStash.get(i));
		}

		System.out.println("Choose a Pokeball (enter the corresponding number) or enter -1 to cancel:");
		Scanner kb = new Scanner(System.in);
		int choice = kb.nextInt();

		if (choice >= 0 && choice < playerPokeballStash.size()) {
			String selectedPokeball = playerPokeballStash.get(choice);
			System.out.println("You selected: " + selectedPokeball);


			Pokeball pokeball = findPokeball(selectedPokeball);


			if (pokeball != null ) {
				double catchChance = Math.random();
				if (catchChance <= pokeball.getCatchRate()) {
					System.out.println("Congratulations! You caught " + enemyPokemons.get(0).getName() + "!");
					Pokemon caughtPokemon = enemyPokemons.remove(0);
					addCaughtPokemon(caughtPokemon);
				} else {
					System.out.println("Oh no! " + enemyPokemons.get(0).getName() + " broke free!");
				}
				playerPokeballStash.remove(choice);
			}
		} else if (choice == -1) {
			System.out.println("Canceled.");
		} else {
			System.out.println("Invalid Input. Try Again.");
			chosePokeball();
		}
		printDivider();
	}
	private static void addCaughtPokemon(Pokemon caughtPokemon) {
		if (playersPokemons.size() < 6) {
			playersPokemons.add(caughtPokemon);
			System.out.println("You caught " + caughtPokemon.getName() +
					"| Type: " + caughtPokemon.getType().toUpperCase());
		} else {
			System.out.println("Your party is full. You can't catch more Pokemon.");
		}
	}

	private static Pokeball findPokeball(String pokeballType) {
		for (Pokeball pokeball : Pokeball.getAllPokeballs()) {
			if (pokeball.getType().equalsIgnoreCase(pokeballType)) {
				return pokeball;
			}
		}
		return null;
	}



	public static void enemyPass() {
		System.out.println(enemyPokemons.get(0).getName() + " Passed!");
		enemyPokemons.get(0).setStuned(false);
		printDivider();
	}

	public static void enemyMove() {
		int enemyEnergy = enemyPokemons.get(0).getEnergy();
		int AttacksIndex = enemyPokemons.get(0).getNumAttacks();
		Random rand = new Random();
		int n = rand.nextInt(AttacksIndex) + 0;
		if (enemyPokemons.get(0).getMoveCost(n) > enemyEnergy) {
			if (enemyPokemons.get(0).getNumAttacks() > 1) {
				if (enemyPokemons.get(0).getMoveCost(0) <= enemyEnergy) {
					System.out.println(enemyPokemons.get(0).getName() + " used " + enemyPokemons.get(0).getMoveName(0) + "!");
					useAttack(enemyPokemons.get(0).getMove(0), "player");
				}
				else if (enemyPokemons.get(0).getMoveCost(1) <= enemyEnergy) {
					System.out.println(enemyPokemons.get(0).getName() + " used " + enemyPokemons.get(0).getMoveName(1) + "!");
					useAttack(enemyPokemons.get(0).getMove(1), "player");
				}
				else {
					enemyPass();
				}
			}

			else {
				enemyPass();
			}
		}
		else {
			System.out.println(enemyPokemons.get(0).getName() + " used " + enemyPokemons.get(0).getMoveName(n) + "!");
			useAttack(enemyPokemons.get(0).getMove(n), "player");
		}
	}

	public static void rawPlayerAttack(String[] move) {
		int damage = 0;
		if (enemyPokemons.get(0).getResistance() == playersPokemons.get(index).getType()) {
			damage = Integer.parseInt(move[2]) / 2;
			System.out.println("not very effective");
		}
		else if (enemyPokemons.get(0).getWeakness() == playersPokemons.get(index).getType()) {
			damage = Integer.parseInt(move[2]) * 2;
			System.out.println("Super Effective!");
		}
		else {
			damage = Integer.parseInt(move[2]);
			System.out.println("Hit!");
		}
		if (playersPokemons.get(index).getDisable() == true) {
			if (damage > 10) {
				damage -= 10;
			} else {
				damage = 0;
			}
		}
		System.out.println("The attack did " + damage + " damage");
		int currHp = enemyPokemons.get(0).getHealth();
		enemyPokemons.get(0).setHealth(currHp - damage);
	}

	public static void rawEnemyAttack(String[] move) {
		int damage = 0;
		if (playersPokemons.get(index).getResistance() == enemyPokemons.get(0).getType()) {
			damage = Integer.parseInt(move[2]) / 2;
			System.out.println("It's not very effective...");
		}
		else if (playersPokemons.get(index).getWeakness() == enemyPokemons.get(0).getType()) {
			damage = Integer.parseInt(move[2]) * 2;
			System.out.println("It's Super Effective!");
		}
		else {
			damage = Integer.parseInt(move[2]);
			System.out.println("Hit!");
		}
		if (enemyPokemons.get(0).getDisable() == true) {
			if (damage > 10) {
				damage -= 10;
			}
			else {
				damage = 0;
			}

		}
		System.out.println("The attack did " + damage + " damage");
		int currHp = playersPokemons.get(index).getHealth();
		playersPokemons.get(index).setHealth(currHp - damage);
	}

	public static void addEnergy() {
		for (int i = 0; i < playersPokemons.size(); i++) {
			int temp = playersPokemons.get(i).getEnergy();
			playersPokemons.get(i).setStuned(false);
			if (temp > 40) {
				playersPokemons.get(i).setEnergy(50);
			}
			else {
				playersPokemons.get(i).setEnergy(temp + 10);
			}
		}

		for (int i = 0; i < enemyPokemons.size(); i++) {
			int current = enemyPokemons.get(i).getEnergy();
			enemyPokemons.get(i).setStuned(false);
			if (current > 40) {
				enemyPokemons.get(i).setEnergy(50);
			}
			else {
				enemyPokemons.get(i).setEnergy(current + 10);
			}
		}


	}

	public static void checkWinner() {
		if (gamerounds ==24) {
			System.out.print(gamerounds);
			System.out.println("CONGRATULATIONS YOU WIN!");
			String trainerName = inputTrainerName();
			writeToHighScoresFile(trainerName,playerHighScore);
			printFileContent("highscores.txt");
			System.exit(0);

		} else if (playersPokemons.isEmpty()) {
			System.out.println("Sadly, you've lost. Try again?");
			String trainerName = inputTrainerName();
			writeToHighScoresFile(trainerName, playerHighScore);
			printFileContent("highscores.txt");
			System.exit(0);
		}

	}
	public static void writeToHighScoresFile(String trainerName, int highScore) {
		try (FileWriter writer = new FileWriter("highscores.txt", true);
			 BufferedWriter bufferedWriter = new BufferedWriter(writer);
			 PrintWriter out = new PrintWriter(bufferedWriter)) {

			out.println(trainerName + ": " + highScore);

		} catch (IOException e) {
			System.err.println("Error writing to the highscores file: " + e.getMessage());
		}
	}

	private static void giveRandomPokeball() {
		Random rand = new Random();
		int ballType = rand.nextInt(4) + 1;

		switch (ballType) {
			case 1:
				playerPokeballStash.add("pokeball");
				System.out.println("You received a Pokeball!");
				break;
			case 2:
				playerPokeballStash.add("greatball");
				System.out.println("You received a Greatball!");
				break;
			case 3:
				playerPokeballStash.add("ultraball");
				System.out.println("You received an Ultraball!");
				break;
			case 4:
				playerPokeballStash.add("masterball");
				System.out.println("You received a Masterball!");
				break;
			default:
				break;
		}
	}
}

