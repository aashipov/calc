var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");

const string statusUp = "{\"status\":\"UP\"}";

app.Map("/{*any}", async (HttpRequest request) =>
{
    if (request.Method.ToLower().Equals("post"))
    {
        try
        {
            using (StreamReader stream = new StreamReader(request.Body))
            {
                string expr = await stream.ReadToEndAsync();
                return (new org.mariuszgromada.math.mxparser.Expression(expr).calculate()).ToString("F14");
            }
        }
        catch (Exception e)
        {
            return e.Message.ToString();
        }
    }
    return statusUp;
}
);

app.Run();
