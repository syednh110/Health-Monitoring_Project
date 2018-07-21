<?php
header("Content-Type: application/json");
if(isset($_POST['input'])){
  $input=$_POST['input'];
  $milliseconds = round(microtime(true) * 1000);
  $file='input'.$milliseconds.'.in';
  $handle = fopen($file, 'w') or die('');
  fwrite($handle, $input);
  fclose($handle);
  ob_start();
  $command='export PATH="/home/namdar/anaconda3/bin:/usr/bin/python3" && python3 project.py < '.$file;
  passthru($command);
  $output=ob_get_clean();
  unlink($file);
  if($output==0){
    print("{\"status\":true,\"result\":false}");
  }
  else{
    print("{\"status\":true,\"result\":true}");
  }
}
else{
  print("{\"status\":false,\"result\":false}");
}
exit();
?>
