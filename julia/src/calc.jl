module calc

using HTTP
using Oxygen
using Base.MathConstants

const WELCOME = "Welcome to calc service\nHTTP POST your expression"

function evaluate(expr::String)::String
    parsed::Expr = Meta.parse(expr)
    result::Float64 = eval(parsed)
    return string(result)
end # function evaluate

@get "/" function (req::HTTP.Request)
    return text(WELCOME)
end

@post "/" function (req::HTTP.Request)
    body::String = text(req)
    eval::String = evaluate(body)
    return text(eval)
end

@post "/mxparser" function (req::HTTP.Request)
    body::String = text(req)
    eval::String = evaluate(body)
    return text(eval)
end

serve(host="0.0.0.0", port=8080)

end # module calc
