# C# flavor

[Debugger](https://github.com/Samsung/netcoredbg)

## Debug in Code OSS

```json
"pipeTransport": {
                "pipeCwd": "${workspaceFolder}",
                "pipeProgram": "sh",
                "pipeArgs": ["-c"],
                "debuggerPath": "netcoredbg"
            }
```

## Debug in Zed

[Extension](https://github.com/qwadrox/zed-netcoredbg)

## Development

```shell
dotnet clean && dotnet build && Logging__LogLevel__Default=Warning dotnet test && Logging__LogLevel__Default=Warning ./bin/Debug/net10.0/calc
```

## Release

```shell
dotnet publish -c Release
```

```shell
Logging__LogLevel__Default=Warning ./bin/Release/net10.0/calc
```
