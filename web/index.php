<?php
  define('APPLICATION', true);
  require_once './internal/headers/config.php';
  require_once './internal/headers/mysql.php';

  session_start();
  if (isset($_SESSION['ApolloEmail']) && !empty($_SESSION['ApolloEmail']) && isset($_SESSION['ApolloUsername']) && !empty($_SESSION['ApolloUsername'])) {
    header('location: home');
    die();
  }

  if (isset($_POST['email']) && !empty($_POST['email']) && isset($_POST['password']) && !empty($_POST['password'])) {
    if (!$Config['recaptcha_enabled'] || (isset($_POST['g-recaptcha-response']) && !empty($_POST['g-recaptcha-response']))) {
      if ($Config['recaptcha_enabled']) {
          $secret = $Config['recaptcha_secret_key'];
          $verifyResponse = file_get_contents('https://www.google.com/recaptcha/api/siteverify?secret=' . $secret . '&response=' . $_POST['g-recaptcha-response']);
          $responseData = json_decode($verifyResponse);
      }
      if (!$Config['recaptcha_enabled'] || $responseData -> success) {
        if ($statement = $MySQL -> prepare('SELECT email, username, password FROM ApolloUsers WHERE email=?;')) {
          $statement -> bind_param('s', $_POST['email']);
          $statement -> execute();
          $statement -> bind_result($email, $username, $hash);
          $statement -> fetch();
          $statement -> close();
          if (password_verify($_POST['password'], $hash)) {
            $_SESSION['ApolloEmail'] = $email;
            $_SESSION['ApolloUsername'] = $username;
            header('location: home');
          } else {
            $error = 'Invalid credentials.';
          }
        } else {
          $error = $MySQL -> error . '.';
        }
      } else {
        $error = 'Robot verification failed, please try again.';
      }
    } else {
      $error = 'Please click on the reCAPTCHA box.';
    }
  } else {
    $error = '';
  }
?>

<!DOCTYPE html>
<html>
  <head>
    <?php include './internal/include/head.inc.php'; ?>

    <title>Login | <?php echo $Config['name'] ?></title>
    <link rel="stylesheet" href="../internal/css/login.css">
  </head>
  <body>
    <div class="center">
      <div class="card-body">
        <h1 class="text-center login-title"><?php echo $Config['name'] ?></h1>
        <form action="/" method="post">
          <input type="text" class="form-control" placeholder="Email" name="email" required autofocus>
          <input type="password" class="form-control" placeholder="Password" name="password" required>

          <?php if ($Config['recaptcha_enabled']) { ?>
            <div class="g-recaptcha" data-sitekey="<?php echo $Config['recaptcha_site_key'] ?>" data-theme="<?php echo $Config['recaptcha_theme'] ?>"></div>
          <?php } ?>

          <button class="btn btn-outline-primary btn-lg btn-block" type="submit">Login</button>

          <?php if (isset($error) && !empty($error)) { ?>
            <div class="alert alert-dismissible alert-danger"><strong><?php echo $error; ?></strong></div>
          <?php } ?>

        </form>
        <?php include './internal/include/footer.inc.php'; ?>
      </div>
    </div>
    
    <?php if ($Config['recaptcha_enabled']) { ?>
      <script type="text/javascript" src="https://www.google.com/recaptcha/api.js"></script>
    <?php } ?>

    <script type="text/javascript" src="../internal/js/logger.js"></script>
    <script type="text/javascript">
      log('[APOLLO] Running version <?php echo $Config["version"]; ?>.', '#f39c12');
    </script>
  </body>
</html>
