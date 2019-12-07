package secret;

import lombok.Value;

@Value
public class DBSecret {
    String username;
    String password;
    String engine;
    String host;
    int port;
    String dbInstanceIdentifier;
}
