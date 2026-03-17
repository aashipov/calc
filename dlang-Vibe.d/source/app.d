import vibe.vibe;

extern (C) double calculate(const char* expression);

const string WELCOME = "Welcome to calc service\nHTTP POST your expression /";
const int DOUBLE_PRECISION = 40;

string doubleToStringWithPrecision(double value, int precision)
{
    return format("%.*g", precision, value);
}

void handler(HTTPServerRequest req, HTTPServerResponse res)
{
    switch (req.method)
    {
    case HTTPMethod.POST:
        {
            const char[] expr = req.bodyReader.readAllUTF8();
            const char* expr_ptr = toStringz(expr);
            const double result = calculate(expr_ptr);
            const string result_string = doubleToStringWithPrecision(result, DOUBLE_PRECISION);
            res.writeBody(result_string);
            break;
        }
    default:
        res.writeBody(WELCOME);
        break;
    }
}

void main()
{
    auto settings = new HTTPServerSettings;
    settings.port = 8080;
    settings.bindAddresses = ["0.0.0.0"];
    auto listener = listenHTTP(settings, &handler);
    scope (exit)
    {
        listener.stopListening();
    }
    runApplication();
}
