# C# implementation

Debug in Code OSS

````
"pipeTransport": {
                "pipeCwd": "${workspaceFolder}",
                "pipeProgram": "bash",
                "pipeArgs": ["-c"],
                "debuggerPath": "netcoredbg"
            }
````

Release

```
dotnet publish -c Release
```
