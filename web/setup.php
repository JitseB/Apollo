<?php
  define('APPLICATION', true);
  require_once './headers/config.php';
  require_once './headers/mysql.php';
?>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <title>Setup | <?php echo $Config['name'] ?></title>
    <link rel="stylesheet" href="<?php echo $Config['style'] ?>">
    <style media="screen">
      .jumbotron {
        height: 50vh;
      }
      .center {
        text-align: center;
      }
    </style>
  </head>
  <body>
    <div class="jumbotron">
      <h1>Setup:</h1>
      <ul>
        <li>
          <?php
          $table =
          'CREATE TABLE IF NOT EXISTS ApolloUsers (
            created BIGINT(50) NOT NULL,
            email VARCHAR(50) NOT NULL,
            username VARCHAR(30) NOT NULL,
            password VARCHAR(255)
          );';

          if ($MySQL -> query($table) === TRUE) {
            echo "Created table ApolloUsers.";
          } else {
            echo "Could not create table: " . $MySQL -> error;
          }
          ?>
        </li>
        <li>
          <?php
          $user = 'INSERT INTO ApolloUsers VALUES ('.time().', "Jitse@fastmail.com", "JitseB", "'.password_hash('testing', PASSWORD_DEFAULT).'");';

          if ($MySQL -> query($user) === TRUE) {
            echo "Created user JitseB.";
          } else {
            echo "Could not create user: " . $MySQL -> error;
          }
          ?>
        </li>
        <li>You may now remove this PHP file.</li>
      </ul>
    </div>
    <p class="center">Thanks for choosing Apollo!</p>
  </body>
</html>
