// This controller handels HTTP Requests For auth Function login/createuser
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;
using System;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using web_service.Helper;
using web_service.Services;
using web_service.Models;
using web_service.Utility;
using web_service.Formatter;

//http request to login
[Route("api/[controller]")]
[ApiController]
public class AuthController : ControllerBase
{
    private readonly IConfiguration _configuration;
    private readonly MongoDBService _mongoDBService;

    public AuthController(IConfiguration configuration, MongoDBService mongoDBService)
    {
        _configuration = configuration;
        _mongoDBService = mongoDBService;
    }

    [HttpPost("login")]
    async public Task<IActionResult> Login([FromBody] LoginProps loginProps)
    {

        try
        {

            TravellerModel traveller = new TravellerModel();
            AdminModel admin = new AdminModel();
            if (loginProps.role == "traveller")
            {

                traveller = await _mongoDBService.Traveller.FindByEmailAsync(loginProps.username ?? "");
            }
            else
            {

                admin = await _mongoDBService.Admin.FindByUsernameAsync(loginProps.username ?? "");
            }


            if ((traveller == null || traveller._id == null) && (admin == null || admin._id == null))
            {
                return NotFound(new ErrorResponse
                {
                    ErrorCode = 404,
                    Message = "Not Found",
                    Details = "Requested User Not Found"
                });
            }


            if (loginProps.role == "traveller" && traveller != null)
            {

                if (!traveller.active)
                {
                    return Unauthorized(new ErrorResponse
                    {
                        ErrorCode = 401,
                        Message = "User Account is inactive.",
                        Details = "Your user account is currently inactive. Please contact admin."
                    });
                }


                if (traveller.VerifyPassword(loginProps.password ?? ""))
                {


                    var token = GenerateJwtToken("traveller", traveller._id ?? "", traveller.email);

                    return Ok(new { token });
                }
                else
                {
                    return Unauthorized(new ErrorResponse
                    {
                        ErrorCode = 401,
                        Message = "Invalid Credentials",
                        Details = "Password or Username Does not Correct"
                    });
                }
            }
            else
            {

                if (!admin.active)
                {
                    return Unauthorized(new ErrorResponse
                    {
                        ErrorCode = 401,
                        Message = "User Account is inactive.",
                        Details = "Your user account is currently inactive. Please contact admin."
                    });
                }


                if (admin.VerifyPassword(loginProps.password ?? ""))
                {


                    var token = GenerateJwtToken(admin.role, admin._id ?? "", admin.username);

                    return Ok(new { token });
                }
                else
                {
                    return Unauthorized(new ErrorResponse
                    {
                        ErrorCode = 401,
                        Message = "Invalid Credentials",
                        Details = "Password or Username Does not Correct"
                    });
                }

            }


        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }


    }
    //jwt generation
    private string GenerateJwtToken(string role, string _id, string username)
    {
        var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]));
        var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

        var claims = new[]
        {
            new Claim(ClaimTypes.Role, role),
            new Claim("uid", _id),
            new Claim("username", username),
        };

        var token = new JwtSecurityToken(
            _configuration["Jwt:Issuer"],
            _configuration["Jwt:Audience"],
            claims,
            expires: DateTime.UtcNow.AddHours(1), // Token expiration time
            signingCredentials: credentials
        );

        return new JwtSecurityTokenHandler().WriteToken(token);
    }
}
