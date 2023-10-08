namespace web_service.Helper
{
    public class AdminBase
    {
        public string? _id { get; set; } = null;
        public string? username { get; set; } = null;

        public string? password { get; set; } = null;

        public string? role
        {
            get; set;
        } = null;

        public bool active { get; set; } = false;

        public bool _isDeleted { get; set; } = false;


    }

    public class TravellerBase
    {

        public string? _id { get; set; }
        public string? NIC { get; set; } = null;
        public string? email { get; set; } = null;
        public string? fullName { get; set; } = null;

        public string? password { get; set; } = null;

        public bool active { get; set; } = false;

        public bool _isDeleted { get; set; } = false;


    }

    public class ReservationBase
    {
        public string? _id { get; set; } = null;

        public string? userID { get; set; } = null;

        public DateTime? date { get; set; } = null;

        public string? train { get; set; } = null;

        public string? trainClass { get; set; }


        public string? travelAgent { get; set; } = null;

        public string? agentNote { get; set; } = null;


        public string? status { get; set; } = "draft";

        public bool _isDeleted { get; set; } = false;


    }

    public class ReservationDetailBase
    {
        public string? _id { get; set; } = null;

        public string? userID { get; set; } = null;

        public DateTime? date { get; set; } = null;

        public string? train { get; set; } = null;

        public string? trainClass { get; set; }


        public string? travelAgent { get; set; } = null;

        public string? agentNote { get; set; } = null;


        public string? status { get; set; } = "draft";

        public bool _isDeleted { get; set; } = false;


    }


    public class TrainScheduleBase
    {
        public string? _id { get; set; }

        public string? trainName { get; set; } = null;

        public string? from { get; set; } = null;

        public string? to { get; set; } = null;

        public DateTime? startTime { get; set; } = null;

        public DateTime? arrivalTime { get; set; } = null;

        public TrainClass? price { get; set; } = null;

        public bool active { get; set; } = false;

        public bool published { get; set; } = false;

        public bool _isDeleted { get; set; } = false;


    }

    public class ResChangePassword
    {
        public string? currentPassword { get; set; } = null;
        public string? newPassword { get; set; } = null;

    }

    public class TrainClass
    {
        public int bussiness { get; set; }
        public int economic { get; set; }
    }

    public class JwtConfiguration
    {
        public string Issuer { get; set; } = null!;
        public string Audience { get; set; } = null!;
        public string Key { get; set; } = null!;
        public int ExpirationSeconds { get; set; } = 0;
    }

    public class LoginProps
    {
        public string username { get; set; } = null!;
        public string password { get; set; } = null!;
        public string role { get; set; } = null!;
    }

    public class TestHelper
    {
        public string? agentNote { get; set; } = null;

    }

}