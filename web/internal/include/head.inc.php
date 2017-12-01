<?php
  if (!defined('APPLICATION') || APPLICATION !== true) {
    die('Unauthorized access of '. pathinfo(__FILE__)['filename'] .'.php.');
  }
 ?>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="<?php echo $Config['style'] ?>">

<link rel="apple-touch-icon" sizes="120x120" href="../internal/icons/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="../internal/icons/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="../internal/icons/favicon-16x16.png">
<link rel="manifest" href="../internal/icons/manifest.json">
<link rel="shortcut icon" href="../internal/icons/favicon.ico">
<meta name="msapplication-config" content="../internal/icons/browserconfig.xml">
<meta name="theme-color" content="#ffffff">
