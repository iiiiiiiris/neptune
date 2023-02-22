# neptune
basic event system

# Usage
Add jitpack to your repositories

**Gradle**
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
**Maven**
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add neptune as a dependency (Replacing {VERSION} with the verison you want) 

**Gradle**
```groovy
dependencies {
  implementation 'com.github.iiiiiiiris:neptune:{VERSION}'
}
```

**Maven**
```xml
<dependency>
  <groupId>com.github.iiiiiiiris</groupId>
  <artifactId>neptune</artifactId>
  <version>{VERSION}</version>
</dependency>
```
