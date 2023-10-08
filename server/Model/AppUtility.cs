namespace web_service.Utility
{
    public class ErrorResponse
    {
        public int ErrorCode { get; set; }
        public string Message { get; set; }
        public string? Details { get; set; }
    }
    public class DataResponse<T>
    {
        public int status { get; set; }

        public T? data { get; set; }
    }
}