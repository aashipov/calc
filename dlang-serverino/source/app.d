import std.format;
import std.string : toStringz;
import serverino;

extern (C) double calculate(const char* expression);

const char[] WELCOME = "Welcome to calc service\nHTTP POST your expression /";
const int DOUBLE_PRECISION = 40;

mixin ServerinoMain;

string doubleToStringWithPrecision(double value, int precision)
{
    return format("%.*g", precision, value);
}

@endpoint
void handler(Request request, Output output)
{
    if (request.method == Request.Method.Post)
    {
        const char[] expr = request.body().data;
        const char* expr_ptr = toStringz(expr);
        const double result = calculate(expr_ptr);
        const string result_string = doubleToStringWithPrecision(result, DOUBLE_PRECISION);
        output.write(result_string);
    }
    else
    {
        output.write(WELCOME);
    }
}
