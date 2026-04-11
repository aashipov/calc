namespace Calc
{
    using Xunit;
    using Microsoft.AspNetCore.Mvc.Testing;
    using System.Net;
    using System.Text;
    using System.Net.Mime;

    public class CalcTests : IClassFixture<WebApplicationFactory<App>>
    {
        private const string SimpleExpression = "2+2";
        private const string SimpleExpressionResult = "4";
        private const string ComplexExpression = "(-abs(pi*2-e-(32-4)/(23+4/5)-(2-4)*(4+6-98.2)+4))+1.9e2";
        private const string ComplexExpressionMxparserResult = "19.98843289048500";
        private const string ComplexExpressionExprtkResult = "19.98843289048523";
        private readonly HttpClient _client;

        public CalcTests(WebApplicationFactory<App> factory)
        {
            _client = factory.CreateClient(new WebApplicationFactoryClientOptions
            {
                AllowAutoRedirect = false
            });
        }

        static async void Check(HttpStatusCode code, string content, HttpResponseMessage response)
        {
            Assert.Equal(code, response.StatusCode);
            string responseBody = await response.Content.ReadAsStringAsync();
            //Console.WriteLine(responseBody);
            Assert.Contains(content, responseBody);
        }

        static StringContent ToStringContent(string s)
        {
            return new(
                s,
                Encoding.UTF8,
                MediaTypeNames.Text.Plain
            );
        }

        [Fact]
        public async Task Welcome()
        {
            HttpResponseMessage response = await _client.GetAsync("/");
            Check(HttpStatusCode.OK, Calc.Welcome, response);
        }

        [Fact]
        public async Task EvaluateSimpleExpressionViaExprtk()
        {
            StringContent content = ToStringContent(SimpleExpression);
            HttpResponseMessage response = await _client.PostAsync("/" + Calc.EXPRTK, content);
            Check(HttpStatusCode.OK, SimpleExpressionResult, response);
        }

        [Fact]
        public async Task EvaluateComplexExpressionViaExprtk()
        {
            StringContent content = ToStringContent(ComplexExpression);
            HttpResponseMessage response = await _client.PostAsync("/" + Calc.EXPRTK, content);
            Check(HttpStatusCode.OK, ComplexExpressionExprtkResult, response);
        }

        [Fact]
        public async Task EvaluateInvalidExpressionViaExprtk()
        {
            StringContent content = ToStringContent(Calc.NaN);
            HttpResponseMessage response = await _client.PostAsync("/" + Calc.EXPRTK, content);
            Check(HttpStatusCode.OK, Calc.NaN, response);
        }

        [Fact]
        public async Task EvaluateSimpleExpressionViaMxparser()
        {
            StringContent content = ToStringContent(SimpleExpression);
            HttpResponseMessage response = await _client.PostAsync("/" + Calc.MXPARSER, content);
            Check(HttpStatusCode.OK, SimpleExpressionResult, response);
        }

        [Fact]
        public async Task EvaluateComplexExpressionViaMxparser()
        {
            StringContent content = ToStringContent(ComplexExpression);
            HttpResponseMessage response = await _client.PostAsync("/" + Calc.MXPARSER, content);
            Check(HttpStatusCode.OK, ComplexExpressionMxparserResult, response);
        }

        [Fact]
        public async Task EvaluateInvalidExpressionViaMxparser()
        {
            StringContent content = ToStringContent(Calc.NaN);
            HttpResponseMessage response = await _client.PostAsync("/" + Calc.MXPARSER, content);
            Check(HttpStatusCode.OK, Calc.NaN, response);
        }
    }
}