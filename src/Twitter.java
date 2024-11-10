import java.util.*;

import javax.swing.GroupLayout.Group;

class LinkedListCommand {
    private class NodeCommand {
        String input;
        NodeCommand next;

        NodeCommand(String input) {
            this.input = input;
        }
    }

    public NodeCommand head;
    public NodeCommand tail;
    public int size;
    public NodeCommand current = head;

    void add(String input) {
        NodeCommand temp = new NodeCommand(input);
        if (size == 0) {
            head = temp;
            tail = temp;
        } else {
            tail.next = temp;
            tail = temp;
        }
        size++;
    }

    String traverseCommand() {
        if (current == null) {
            current = head;
        } else {
            current = current.next;
        }
        return current != null ? current.input : null;
    }
}

class Twitter {
    private class Akun {
        private String userName;
        private String[] minat;
        private int follower;

        Akun(String userName, String minat1, String minat2, String minat3) {
            this.userName = userName.toLowerCase();
            this.minat = new String[] { minat1, minat2, minat3 };
        }

        public String getUserName() {
            return userName;
        }

        public int getFollower() {
            return follower;
        }
        public String[] getMinat(){
            return minat;
        }
    }

    private class Group {
        private Akun[] members;
        private int size;

        Group() {
            members = new Akun[numUsers];
            size = 0;
        }

        void addMember(Akun akun) {
            members[size++] = akun;
        }

        Akun getMember(int index) {
            return members[index];
        }

        int getSize() {
            return size;
        }
    }
    private Akun[] users;
    private int[][] adjList;
    private int numUsers;
    private int count = 0;

    Twitter(int numUsers) {
        this.numUsers = numUsers;
        this.users = new Akun[this.numUsers];
        this.adjList = new int[this.numUsers][this.numUsers];
    }

    private void insertUser(String userName, String minat1, String minat2, String minat3) {
        Akun newUser = new Akun(userName, minat1, minat2, minat3);
        users[count++] = newUser;
        System.out.println(newUser.getUserName() + " inserted");
    }

    private int findUserIndex(String userName) {
        for (int i = 0; i < count; i++) {
            if (users[i].getUserName().equalsIgnoreCase(userName)) {
                return i;
            }
        }
        return -1;
    }

    private void connect(String userName1, String userName2) {
        int index1 = findUserIndex(userName1);
        int index2 = findUserIndex(userName2);
        if (index1 != -1 && index2 != -1) {
            adjList[index1][index2] = 1;
            users[index2].follower++;
            System.out.printf("connect %s %s success\n", userName1, userName2);
        } else {
            System.out.println("One or both users not found");
        }
    }

    private void mostFollowed() {
        int maxFollowers = 0;
        Akun mostFollowedUser = null;

        for (int i = 0; i < count; i++) {
            Akun currentUser = users[i];
            int followers = currentUser.getFollower();

            if (followers > maxFollowers) {
                maxFollowers = followers;
                mostFollowedUser = currentUser;
            }
        }

        if (mostFollowedUser != null) {
            System.out.printf("Most followed user: %s with %d followers\n", mostFollowedUser.getUserName(),
                    maxFollowers);
        } else {
            System.out.println("No users found.");
        }
    }

    private void dfs(int userIndex, boolean[] visited, Group group) {
        visited[userIndex] = true;
        group.addMember(users[userIndex]);
        for (int i = 0; i < numUsers; i++) {
            if((adjList[userIndex][i] == 1 || adjList[i][userIndex] == 1)   && !visited[i]){
                dfs(i, visited, group);
            }
            
        }
    }
    

    private int numgroup() {
        boolean[] visited = new boolean[numUsers];
        int countGroup = 0;
        for (int i = 0; i < count; i++) {
            if (!visited[i]) {
                Group group = new Group();
                dfs(i, visited, group );
                countGroup++;
            }
        }
        return countGroup;
    }

    

    private void mincuit(String startUserName, String targetUserName) {
        int startIndex = findUserIndex(startUserName);
        int targetIndex = findUserIndex(targetUserName);

        if (startIndex == -1 || targetIndex == -1) {
            System.out.println(-1);
        }

        int[][] distances = new int[numUsers][numUsers];

        for (int i = 0; i < numUsers; i++) {
            for (int j = 0; j < numUsers; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else if (adjList[i][j] != 0) {
                    distances[i][j] = adjList[i][j];
                } else {
                    distances[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        for (int k = 0; k < numUsers; k++) {
            for (int i = 0; i < numUsers; i++) {
                for (int j = 0; j < numUsers; j++) {
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE
                            && distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                    }
                }
            }
        }

        int distance = distances[startIndex][targetIndex];
        if (distance != Integer.MAX_VALUE) {
            System.out.println(distance - 1);
        } else {
            System.out.println(-1);
        }
    }

    private Group[] getAllGroups() {
        boolean[] visited = new boolean[numUsers];
        Group[] groups = new Group[numUsers];
        int groupCount = 0;

        for (int i = 0; i < count; i++) {
            if (!visited[i]) {
                Group group = new Group();
                dfs(i, visited, group);
                groups[groupCount++] = group;
            }
        }

        Group[] result = new Group[groupCount];
        System.arraycopy(groups, 0, result, 0, groupCount);
        return result;
    }

    public void printPopularTopics() {
        Group[] groups = getAllGroups();

        for (int i = 0; i < groups.length; i++) {
            printPopularTopicsForGroup(groups[i]);
        }
    }

    private void printPopularTopicsForGroup(Group group) {
        String[] allTopics = new String[numUsers * 3];
        int topicCount = 0;
    
        for (int i = 0; i < group.getSize(); i++) {
            Akun akun = group.getMember(i);
            for (String topic : akun.getMinat()) {
                allTopics[topicCount++] = topic;
            }
        }
    
        String[] uniqueTopics = new String[topicCount];
        int[] topicFrequency = new int[topicCount];
        int uniqueCount = 0;
    
        for (int i = 0; i < topicCount; i++) {
            String topic = allTopics[i];
            boolean found = false;
            for (int j = 0; j < uniqueCount; j++) {
                if (uniqueTopics[j].equals(topic)) {
                    topicFrequency[j]++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                uniqueTopics[uniqueCount] = topic;
                topicFrequency[uniqueCount] = 1;
                uniqueCount++;
            }
        }
    
        int maxFrequency = 0;
        for (int i = 0; i < uniqueCount; i++) {
            if (topicFrequency[i] > maxFrequency) {
                maxFrequency = topicFrequency[i];
            }
        }
    
        
        String[] mostPopularTopics = new String[uniqueCount];
        int popularCount = 0;
    
        for (int i = 0; i < uniqueCount; i++) {
            if (topicFrequency[i] == maxFrequency) {
                mostPopularTopics[popularCount++] = uniqueTopics[i];
            }
        }
    
        for (int i = 0; i < popularCount - 1; i++) {
            for (int j = i + 1; j < popularCount; j++) {
                if (mostPopularTopics[i].compareTo(mostPopularTopics[j]) > 0) {
                    String temp = mostPopularTopics[i];
                    mostPopularTopics[i] = mostPopularTopics[j];
                    mostPopularTopics[j] = temp;
                }
            }
        }
    
        for (int i = 0; i < popularCount; i++) {
            System.out.print(mostPopularTopics[i]);
            if (i < popularCount - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    
    public void cetak(String komentar) {
        System.out.println(komentar);
        for (int i = 0; i < count; i++) {
            System.out.print("[" + i + "]");
            for (int j = 0; j < count; j++) {
                if (adjList[i][j] == 1) {
                    System.out.print("->" + users[j].getUserName());
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        LinkedListCommand commandList = new LinkedListCommand();
        Scanner sc = new Scanner(System.in);

        int numUsers = sc.nextInt();
        sc.nextLine(); // Consume the newline character

        Twitter graph = new Twitter(numUsers);

        while (true) {
            String inputCommand = sc.nextLine();
            if (inputCommand.isEmpty()) {
                break;
            } else {
                commandList.add(inputCommand);
            }
        }

        for (int i = 0; i < commandList.size; i++) {
            String input = commandList.traverseCommand();
            String[] split = input.split(" ");
            switch (split[0]) {
                case "insert":
                    graph.insertUser(split[1], split[2], split[3], split[4]);
                    break;
                case "connect":
                    graph.connect(split[1], split[2]);
                    break;
                case "numgroup":
                    int numgroup = graph.numgroup();
                    System.out.println(numgroup);
                    break;
                case "cetak":
                    graph.cetak("graph sekarang :");
                    break;
                case "mostfollowed":
                    graph.mostFollowed();
                    break;
                case "mincuit":
                    graph.mincuit(split[1], split[2]);
                    break;
                case "grouptopic":
                    graph.printPopularTopics();
                default:
                    break;
            }
        }
    }
}