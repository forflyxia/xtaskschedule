<!DOCTYPE html>

<html lang="en">

<body>
Logs!
<div style="background-color: black; font-size: small; color:white;">
    <#list logs as logs>
        <div>${logs.log}</div><br/>
    </#list>
</div>
</body>

</html>