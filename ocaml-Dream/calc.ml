open Ctypes

let foreign name cExprtkAdapter =
  Foreign.foreign name cExprtkAdapter ~from:(Dl.dlopen ~filename:"libc-exprtk-adapter.so" ~flags:[RTLD_NOW])

let calculate = foreign "calculate" (string @-> returning double)

let post_handler request =
  let%lwt expr = Dream.body request in
      let result = calculate expr in
      let result_string = string_of_float result in
      Dream.respond
        ~headers:["Content-Type", "text/plain"]
        result_string

let () =
  Dream.run
  @@ Dream.router [

    Dream.get  "/" (fun _ ->
      Dream.respond
        ~headers:["Content-Type", "text/plain"]
        "Welcome to calc service\nHTTP POST your expression /\n");

    Dream.post "/" post_handler;

    Dream.post "/mxparser" post_handler;

    Dream.post "/exprtk" post_handler;
      
  ]
