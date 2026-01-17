<?php
    $dbhost = "localhost";  
    $dbuser = "root";  
    $dbpassword = "";  
    $dbname = "biblio";  
    $dblink = mysqli_connect($dbhost, $dbuser, $dbpassword, $dbname);
    function qdb($sql) {
        global $dblink;
        $resultat = mysqli_query($dblink, $sql);
        
        if (!$resultat) {
            echo "MySQL error : " . mysqli_error($dblink) . "<br>";
            echo "Requête MySQL : " . $sql . "<br>";
            die;
        }
        return $resultat;
    }
?>
