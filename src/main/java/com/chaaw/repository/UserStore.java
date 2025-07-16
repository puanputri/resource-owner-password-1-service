import com.chaaw.model.User;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
public class UserStore {
    private final Map<String, User> users = Map.of(
            "alice", new User("alice", "password123", "alice@example.com"),
            "bob", new User("bob", "hunter2", "bob@example.com")
    );

    public User find(String username) {
        return users.get(username);
    }
}
