import java.util.*;

class Player {
    String name;
    int points;
    boolean isJudge;

    public Player(String name, int points, boolean isJudge) {
        this.name = name;
        this.points = points;
        this.isJudge = isJudge;
    }

    @Override
    public String toString() {
        return name + (isJudge ? " (Judge)" : "") + " - Points: " + points;
    }
}

class Tournament {
    private List<Player> players;
    private Player judge;

    public Tournament(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.judge = players.stream().filter(p -> p.isJudge).findFirst().orElse(null);
        if (this.judge == null) {
            throw new IllegalArgumentException("No judge found in the player list");
        }
    }

    public void start() {
        System.out.println("\n=== Pairing Teams ===");
        List<List<Player>> teams = formTeams();
        List<List<List<Player>>> matches = createBalancedMatches(teams);
        displayMatches(matches); // Выводим матчи
        inputResults(matches);
        displayScores();
    }

    private List<List<Player>> formTeams() {
        List<Player> availablePlayers = new ArrayList<>(players);
        availablePlayers.remove(judge);
        List<List<Player>> teams = new ArrayList<>();

        // Сортировка игроков по очкам
        availablePlayers.sort(Comparator.comparingInt(p -> p.points));
        int n = availablePlayers.size();

        // Формируем команды из двух игроков
        for (int i = 0; i < n / 2; i++) {
            teams.add(Arrays.asList(availablePlayers.get(i), availablePlayers.get(n - 1 - i)));
        }

        // Оставшийся игрок формирует отдельную команду
        if (n % 2 != 0) {
            teams.add(Collections.singletonList(availablePlayers.get(n / 2)));
        }

        System.out.println("Formed Teams:");
        for (List<Player> team : teams) {
            System.out.println(team);
        }

        return teams;
    }

    private List<List<List<Player>>> createBalancedMatches(List<List<Player>> teams) {
        List<List<List<Player>>> matches = new ArrayList<>();
        List<Player> remainingPlayers = new ArrayList<>();

        // Разделяем на пары с учётом очков
        while (teams.size() > 1) {
            List<Player> team1 = teams.remove(0);
            List<Player> team2 = teams.remove(0);
            matches.add(Arrays.asList(team1, team2));
        }

        // Если остался одиночный игрок, он играет против судьи
        if (teams.size() == 1) {
            matches.add(Arrays.asList(Collections.singletonList(judge), teams.get(0)));
        }

        return matches;
    }

    private void displayMatches(List<List<List<Player>>> matches) {
        System.out.println("\nMatches:");
        for (List<List<Player>> match : matches) {
            System.out.println(match.get(0) + " vs " + match.get(1));
        }
    }

    private void inputResults(List<List<List<Player>>> matches) {
        Scanner scanner = new Scanner(System.in);
        for (List<List<Player>> match : matches) {
            System.out.println("\nEnter result for match: " + match.get(0) + " vs " + match.get(1));
            System.out.println("1 - First team wins, 2 - Second team wins, 3 - Draw");
            int result = scanner.nextInt();
            switch (result) {
                case 1:
                    updatePoints(match.get(0), 3);
                    break;
                case 2:
                    updatePoints(match.get(1), 3);
                    break;
                case 3:
                    updatePoints(match.get(0), 1);
                    updatePoints(match.get(1), 1);
                    break;
                default:
                    System.out.println("Invalid input, skipping result.");
            }
        }
    }

    private void updatePoints(List<Player> team, int points) {
        for (Player player : team) {
            player.points += points;
        }
    }

    private void displayScores() {
        System.out.println("\nCurrent Scores:");
        players.forEach(System.out::println);
    }
}

public class Main {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Kiban", 0, true)); // Судья
        players.add(new Player("Artem Malyshev", 9, false));
        players.add(new Player("Artem Petrov", 9, false));
        players.add(new Player("Ivan Stepanov", 6, false));
        players.add(new Player("Yan", 6, false));
        players.add(new Player("Niko", 6, false));
        players.add(new Player("Arto", 6, false));
        players.add(new Player("Dimitri Molokanov", 3, false));
        players.add(new Player("Dimitri", 3, false));
        players.add(new Player("Vasiliy", 3, false));
        players.add(new Player("Timofey", 3, false));
        players.add(new Player("Georgii", 0, false));
        players.add(new Player("Irakli", 0, false));
        players.add(new Player("Valerian", 0, false));

        Collections.shuffle(players);
        Tournament tournament = new Tournament(players);
        tournament.start();
    }
}
