using web_service.Utility;
namespace web_service.Formatter
{
    public class ErrorFormatter
    {

        private System.Exception error;
        public ErrorFormatter(System.Exception ex)
        {
            error = ex;
        }

        public ErrorResponse Get(string? notfoundMsg)
        {
            if (error is MongoDB.Driver.MongoWriteException)
            {
                // System.Console.WriteLine();
                if (error.Message.Contains("Category : \"DuplicateKey\""))
                {

                    return new ErrorResponse
                    {
                        ErrorCode = 400,
                        Message = notfoundMsg != null ? notfoundMsg : "Duplicate Field Found",
                        Details = error.Message
                    };

                }
                else
                {
                    return new ErrorResponse
                    {
                        ErrorCode = 500,
                        Message = "Internal Server Error",
                        Details = error.Message
                    };


                }
            }
            else
            {

                return new ErrorResponse
                {
                    ErrorCode = 500,
                    Message = "Internal Server Error",
                    Details = error.Message
                };

            }

        }
    }
}