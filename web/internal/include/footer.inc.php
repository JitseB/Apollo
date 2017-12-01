<?php
  if (!defined('APPLICATION') || APPLICATION !== true) {
    die('Unauthorized access of '. pathinfo(__FILE__)['filename'] .'.php.');
  }
 ?>

 <div class="text-center">
   <span>© <?php echo (new DateTime()) -> format('Y'); ?> - Apollo - Jitse Boonstra</span><br>
   <span>A server overview system for Minecraft networks.</span><br>
   <!--<span>View source code on <a href="https://github.com/JitseB/Apollo">GitHub</a>.</span>-->
 </div>
