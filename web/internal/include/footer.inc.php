<?php
  if (!defined('APPLICATION') || APPLICATION !== true) {
    die('Unauthorized access of '. pathinfo(__FILE__)['filename'] .'.php.');
  }
 ?>

 <div class="text-center">
   <span><?php echo $Config['network_name'] ?></span><br>
   <span>© <?php echo (new DateTime()) -> format('Y'); ?> - Apollo - Jitse B.</span><br>
   <span>A server overview system for Minecraft networks.</span><br>
 </div>
