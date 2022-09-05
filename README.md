### Build from source


Clone the source code from Git, use the following command to build

` $ cd identity-hub `


Perform all build tasks (including integrated unit tests)

`$ mvn clean install`


If you only want to execute the build of the server package without executing the test, execute the following command

`$ mvn clean install -Dmaven.test.skip=true`

### Maven
```
<dependency>
  <groupId>com.reddate</groupId>
  <artifactId>hub.sdk</artifactId>
  <version>${version}</version>
</dependency>
```


### Gradle
```
compile ('com.reddate:hub.sdk:${version}')
```

### The first class 

```
public HubClient(String url) {

  this(url, "", "", CryptoType.ECDSA);
}
```

### Call interface

```
HubClient hubClient = new HubClient(url);

RegisterHubResult registerHubResult = hubClient.registerHub(publicKey, CryptoType.ECDSA);
```


	
 	
	