When working with databases, understanding the **SQL data type mappings** to Java types is essential. Here's a comprehensive breakdown of how common SQL data types map to Java types, including `INTEGER`, `BIGINT`, and others.

---

### **1. SQL to Java Data Type Mappings**

#### **Integer Types:**

| **SQL Type**       | **Java Type**         | **Notes**                                                     |
|--------------------|-----------------------|---------------------------------------------------------------|
| `INT` / `INTEGER`   | `Integer`             | Maps to 32-bit signed integer. Suitable for small numbers.    |
| `TINYINT`           | `Byte`                | Maps to a 1-byte integer (-128 to 127).                       |
| `SMALLINT`          | `Short`               | Maps to a 2-byte integer (-32,768 to 32,767).                 |
| `MEDIUMINT`         | `Integer`             | Maps to a 3-byte integer, used mostly in MySQL.                |

#### **Big Integer Types:**

| **SQL Type**       | **Java Type**         | **Notes**                                                     |
|--------------------|-----------------------|---------------------------------------------------------------|
| `BIGINT`           | `Long`                | Maps to a 64-bit signed integer. Suitable for very large numbers. |
| `DECIMAL` / `NUMERIC` | `BigDecimal`        | Used for exact precision arithmetic, typically for financial values. |
| `FLOAT`            | `Float`               | Maps to 4-byte floating point numbers (approx. 7 digits).    |
| `DOUBLE`           | `Double`              | Maps to 8-byte floating point numbers (approx. 15 digits).   |

#### **String and Character Types:**

| **SQL Type**       | **Java Type**         | **Notes**                                                     |
|--------------------|-----------------------|---------------------------------------------------------------|
| `CHAR`             | `String`              | Fixed-length string (1 to 255 characters).                    |
| `VARCHAR`          | `String`              | Variable-length string, up to 65,535 characters in MySQL.     |
| `TEXT`             | `String`              | Text with variable length, used for larger text blocks.       |

#### **Date and Time Types:**

| **SQL Type**       | **Java Type**         | **Notes**                                                     |
|--------------------|-----------------------|---------------------------------------------------------------|
| `DATE`             | `java.sql.Date`       | Stores only date (yyyy-mm-dd).                                |
| `TIME`             | `java.sql.Time`       | Stores only time (hh:mm:ss).                                  |
| `DATETIME`         | `java.sql.Timestamp`  | Stores both date and time (yyyy-mm-dd hh:mm:ss).              |
| `TIMESTAMP`        | `java.sql.Timestamp`  | Stores both date and time, often used for auto-updating fields. |

#### **Boolean Types:**

| **SQL Type**       | **Java Type**         | **Notes**                                                     |
|--------------------|-----------------------|---------------------------------------------------------------|
| `BOOLEAN`          | `Boolean`             | Stores true/false values.                                     |
| `TINYINT(1)`       | `Boolean`             | In MySQL, `TINYINT(1)` is often used to represent boolean values. |

#### **Binary Types:**

| **SQL Type**       | **Java Type**         | **Notes**                                                     |
|--------------------|-----------------------|---------------------------------------------------------------|
| `BLOB`             | `byte[]`              | Binary large object for storing binary data.                  |
| `BINARY`           | `byte[]`              | Fixed-length binary data.                                     |
| `VARBINARY`        | `byte[]`              | Variable-length binary data.                                  |

---

### **2. SQL Type Mapping to Java Types in Detail**

#### **`INT` / `INTEGER` to `Integer` (32-bit)**

In SQL, `INT` or `INTEGER` stores a 32-bit signed integer. This maps to the `Integer` class in Java, which stores values from `-2,147,483,648` to `2,147,483,647`.

Example:
```sql
CREATE TABLE movie (
    movie_id INT PRIMARY KEY,
    movie_name VARCHAR(255)
);
```

Java:
```java
public class Movie {
    private Integer movieId;  // Integer maps to INT
    private String movieName;
}
```

#### **`BIGINT` to `Long` (64-bit)**

In SQL, `BIGINT` stores a 64-bit signed integer, which maps to the `Long` class in Java. The `Long` type in Java has a larger range (`-9,223,372,036,854,775,808` to `9,223,372,036,854,775,807`), making it ideal for very large numbers.

Example:
```sql
CREATE TABLE movie (
    movie_id BIGINT PRIMARY KEY,
    movie_name VARCHAR(255)
);
```

Java:
```java
public class Movie {
    private Long movieId;  // Long maps to BIGINT
    private String movieName;
}
```

#### **`DECIMAL` / `NUMERIC` to `BigDecimal`**

`DECIMAL` or `NUMERIC` types store precise decimal values (usually for financial calculations). Java maps these to `BigDecimal`, which can handle arbitrary-precision arithmetic.

Example:
```sql
CREATE TABLE movie (
    price DECIMAL(10, 2)  -- Price with two decimal places
);
```

Java:
```java
import java.math.BigDecimal;

public class Movie {
    private BigDecimal price;  // BigDecimal maps to DECIMAL or NUMERIC
}
```

#### **`CHAR` and `VARCHAR` to `String`**

For textual data, SQL `CHAR` and `VARCHAR` types are mapped to the `String` type in Java. The difference is that `CHAR` is a fixed-length string, while `VARCHAR` is variable-length.

Example:
```sql
CREATE TABLE movie (
    movie_name VARCHAR(255)  -- Variable length string
);
```

Java:
```java
public class Movie {
    private String movieName;  // String maps to VARCHAR
}
```

#### **`DATE`, `TIME`, `DATETIME`, `TIMESTAMP` to `java.sql.Date` and `java.sql.Timestamp`**

SQL `DATE`, `TIME`, `DATETIME`, and `TIMESTAMP` types are mapped to Java's `java.sql.Date` and `java.sql.Timestamp`.

- `DATE` maps to `java.sql.Date` (only date).
- `TIME` maps to `java.sql.Time` (only time).
- `DATETIME` and `TIMESTAMP` map to `java.sql.Timestamp` (date and time).

Example:
```sql
CREATE TABLE movie (
    release_date DATETIME  -- Date and time
);
```

Java:
```java
import java.sql.Timestamp;

public class Movie {
    private Timestamp releaseDate;  // Timestamp maps to DATETIME
}
```

#### **`BOOLEAN` to `Boolean`**

SQL `BOOLEAN` (or sometimes `TINYINT(1)` in MySQL) maps to Java's `Boolean` class.

Example:
```sql
CREATE TABLE movie (
    is_active BOOLEAN  -- True/False flag
);
```

Java:
```java
public class Movie {
    private Boolean isActive;  // Boolean maps to BOOLEAN
}
```

#### **`BLOB`, `BINARY`, `VARBINARY` to `byte[]`**

For binary data, SQL `BLOB`, `BINARY`, and `VARBINARY` types map to `byte[]` in Java, which is an array of bytes.

Example:
```sql
CREATE TABLE movie (
    poster BLOB  -- Store image or binary data
);
```

Java:
```java
public class Movie {
    private byte[] poster;  // byte[] maps to BLOB
}
```

---

### **3. Special Notes for MyBatis or ORM Frameworks**

When using MyBatis, JPA, or Hibernate, it is essential that your Java class fields match the SQL column types. The framework will automatically map the SQL types to the appropriate Java types.

For example, MyBatis provides a mapping between SQL types and Java types. If you have a `BIGINT` in the database, it will map to a `Long` in Java:

#### **MyBatis Mapping Example:**

```xml
<resultMap id="movieResult" type="com.example.Movie">
    <id column="movie_id" property="movieId" />
    <result column="movie_name" property="movieName" />
    <result column="release_date" property="releaseDate" />
</resultMap>

<select id="getMovieById" resultMap="movieResult">
    SELECT movie_id, movie_name, release_date FROM movie WHERE movie_id = #{id}
</select>
```

Here, `movie_id` maps to `Long` (`BIGINT`) and `release_date` maps to `Timestamp` (`DATETIME`).

---

### **4. Conclusion**

Here’s a quick summary:

- **Integer Types** (`INT`, `INTEGER`, `SMALLINT`, `TINYINT`) map to `Integer`, `Short`, or `Byte` in Java.
- **Big Integer Types** (`BIGINT`) map to `Long`.
- **Floating Types** (`FLOAT`, `DOUBLE`) map to `Float` and `Double`.
- **Text Types** (`VARCHAR`, `CHAR`) map to `String`.
- **Date/Time Types** (`DATE`, `TIME`, `DATETIME`, `TIMESTAMP`) map to `java.sql.Date` or `java.sql.Timestamp`.
- **Boolean

Types** (`BOOLEAN`) map to `Boolean`.
- **Binary Types** (`BLOB`, `BINARY`, `VARBINARY`) map to `byte[]`.

Ensure that your Java class attributes are correctly annotated for ORM or database frameworks like MyBatis, Hibernate, etc., so that type mappings are correctly handled.