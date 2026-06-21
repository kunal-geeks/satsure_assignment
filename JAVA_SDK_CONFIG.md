# Java SDK Configuration

## Current System Configuration

**Installed Java Version**: `OpenJDK 22.0.1` (build 22.0.1+8-16)

```
openjdk version "22.0.1" 2024-04-16
OpenJDK Runtime Environment (build 22.0.1+8-16)
OpenJDK 64-Bit Server VM (build 22.0.1+8-16, mixed mode, sharing)
```

---

## Project Configuration Files Updated

### 1. **pom.xml** (Maven Configuration)
✅ Updated to use Java 22:
```xml
<maven.compiler.source>22</maven.compiler.source>
<maven.compiler.target>22</maven.compiler.target>
```

### 2. **.java-version** (SDK Manager Configuration)
✅ Created for tools like SDKMAN!, jenv, and IDE auto-detection:
```
java=22.0.1
```

### 3. **.vscode/settings.json** (VS Code IDE Configuration)
✅ Configured for VS Code to automatically use Java 22:
```json
{
  "java.jdt.ls.java.home": "/usr/libexec/java_home -v 22",
  "java.configuration.runtimes": [
    {
      "name": "JavaSE-22",
      "path": "/usr/libexec/java_home -v 22",
      "default": true
    }
  ]
}
```

### 4. **README.md** (Documentation)
✅ Updated prerequisites:
```
- Java 22.0.1 or compatible version
- Maven 3.6+
```

---

## IDE Setup Instructions

### **VS Code**
1. Install Extensions:
   - Extension Pack for Java (Microsoft)
   - Maven for Java

2. Open project in VS Code
3. The `.vscode/settings.json` will automatically load
4. Reload window: `Cmd + Shift + P` → "Reload Window"
5. Verify: `Java: Show Java Runtime Information`

### **IntelliJ IDEA**
1. Open project
2. Go to: **File → Project Structure → Project**
3. Set **SDK** to: `openjdk-22.0.1`
4. Set **Language Level** to: `22`
5. Click **Apply** and **OK**

### **Eclipse**
1. Right-click project → **Properties**
2. **Java Compiler** → Set **Compiler compliance level** to `22`
3. **Project Facets** → Set **Java** to `22`

### **Maven Command Line**
```bash
# Verify Java version
java -version
javac -version

# Install dependencies with Java 22
mvn clean install

# Run tests with Java 22
mvn clean test
```

---

## Verification

### Check Maven Configuration
```bash
mvn -v
```

### Check Compiler Version
```bash
javac -version
```

### Verify Project Builds
```bash
cd /Users/kunalsharma/Downloads/satsure_assignment
mvn clean compile
```

---

## Java 22 Features Available

This project can now use Java 22 features:
- ✅ Records (Java 16+)
- ✅ Sealed Classes (Java 17+)
- ✅ Pattern Matching for instanceof (Java 16+)
- ✅ Text Blocks (Java 15+)
- ✅ Unnamed Variables (Java 21+)
- ✅ Foreign Function & Memory API (Preview)
- ✅ Structured Concurrency (Preview)

---

## Troubleshooting

### Issue: "Wrong Java version" error
**Solution**: 
```bash
# Set JAVA_HOME explicitly
export JAVA_HOME=$(/usr/libexec/java_home -v 22)
java -version  # Should show 22.0.1
mvn clean test  # Will use Java 22
```

### Issue: IDE still shows Java 11
**Solution**:
1. Clear IDE cache
2. Delete `.idea` folder (IntelliJ) or `.vscode` workspace settings
3. Restart IDE
4. Re-open project

### Issue: Maven uses wrong Java version
**Solution**:
```bash
# Force Maven to use Java 22
mvn -Dmaven.compiler.source=22 -Dmaven.compiler.target=22 clean test
```

---

## Summary

✅ **System Java Version**: `22.0.1` (OpenJDK)  
✅ **Project Configured for**: `Java 22`  
✅ **Maven**: Updated to use Java 22  
✅ **VS Code**: Auto-configured for Java 22  
✅ **Ready to Build**: Yes  

The project is now fully configured to use Java 22.0.1 across all development environments.

---

**Last Updated**: June 21, 2026  
**Status**: ✅ Java SDK Configuration Complete
