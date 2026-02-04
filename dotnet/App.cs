namespace Calc
{
    public class App
    {
        static void Main(string[] args)
        {
            WebApplication app = Calc.Initialize(args);
            app.Run();
        }
    }
}
