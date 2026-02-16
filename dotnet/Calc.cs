namespace Calc
{
    using System.Net.Mime;
    using Microsoft.AspNetCore.Http.Extensions;

    public static class Calc
    {
        [System.Runtime.InteropServices.DllImport("c-exprtk-adapter")]
        static extern double calculate(string expression);

        public const string Welcome = "Welcome to calc service\nHTTP POST your expression\n";

        public const string NaN = "NaN";

        public const string EXPRTK = "exprtk";

        static async Task<IResult> Get()
        {
            return TypedResults.Content(Welcome, MediaTypeNames.Text.Plain);
        }

        static async Task<IResult> Post(HttpRequest request)
        {
            string responseText = NaN;
            try
            {
                using StreamReader stream = new(request.Body, System.Text.Encoding.UTF8);
                string expr = await stream.ReadToEndAsync();
                double result = double.NaN;
                if (request.GetDisplayUrl().Contains(EXPRTK))
                {
                    result = calculate(expr);
                }
                else
                {
                    result = new org.mariuszgromada.math.mxparser.Expression(expr).calculate();
                }
                responseText = result.ToString("F14");
            }
            catch (Exception e)
            {
                responseText = e.Message.ToString();
            }
            return TypedResults.Content(responseText, MediaTypeNames.Text.Plain);
        }

        public static WebApplication Initialize(string[] args)
        {
            org.mariuszgromada.math.mxparser.License.iConfirmNonCommercialUse("dummy");
            WebApplicationBuilder builder = WebApplication.CreateBuilder(new WebApplicationOptions
            {
                Args = args,
                ContentRootPath = AppContext.BaseDirectory
            });
            builder.WebHost.ConfigureKestrel(serverOptions => { serverOptions.ListenAnyIP(8080); });
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();
            WebApplication app = builder.Build();
            app.MapGet("/{*any}", Get);
            app.MapPost("/{*any}", Post).Accepts<string>(MediaTypeNames.Text.Plain);
            app.UseSwagger();
            app.UseSwaggerUI(options =>
            {
                options.SwaggerEndpoint("/swagger/v1/swagger.json", "v1");
                options.RoutePrefix = "openapi-ui";
            });
            return app;
        }
    }
}
