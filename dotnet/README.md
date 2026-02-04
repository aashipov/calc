# C# implementation #

[Debugger](https://github.com/Samsung/netcoredbg)

## Debug in Code OSS ##

```json
"pipeTransport": {
                "pipeCwd": "${workspaceFolder}",
                "pipeProgram": "sh",
                "pipeArgs": ["-c"],
                "debuggerPath": "netcoredbg"
            }
```

## Debug in Zed ##

[Extension](https://github.com/qwadrox/zed-netcoredbg)

## Development ##

```shell
dotnet clean && dotnet build && dotnet test && ./bin/Debug/net10.0/calc
```

## Release ##

```shell
dotnet publish -c Release
```
