<?php
  define('APPLICATION', true);
  require_once '../internal/headers/config.php';
?>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error | <?php echo $Config['name'] ?></title>
    <link rel="stylesheet" href="<?php echo $Config['style'] ?>">
    <style media="screen">
      .nb-error {
        margin: 0 auto;
        text-align: center;
        max-width: 480px;
        padding: 60px 30px;
      }
      .nb-error h1 {
       font-size: 80px;
      }
    </style>
  </head>
  <body>
    <div class="nb-error">
      <h1><strong>Uh ow.</strong></h1>
      <h3 class="font-bold">That's a 404.</h3><br>
      <div class="error-desc">
        <div class="alert alert-dismissible alert-danger">
          <strong>We could not find the page you were looking for...</strong><br><br>
          Try refreshing the page or click the button below to go back to the login page.
        </div><br>
        <a class="btn btn-outline-danger" href="../">Login</a><br><br><br>
        <?php include '../internal/include/footer.inc.php'; ?>
      </div>
    </div>
  </body>
</html>
