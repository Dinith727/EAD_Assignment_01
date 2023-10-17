/*
Handling HTTP requests on Travellers
POST , GET , DELETE , DEACTIVATE/ACTIVATE
*/
using Microsoft.AspNetCore.Mvc;
using System;
using web_service.Services;
using web_service.Models;
using web_service.Utility;
using web_service.Helper;
using web_service.Formatter;
using Microsoft.AspNetCore.Authorization;
using Microsoft.IdentityModel.Tokens;
using System.Security.Claims;


namespace web_service.Controllers;

[ApiController]
[Route("api/[controller]")]
public class TravellerController : ControllerBase
{

    private readonly MongoDBService _mongoDBService;

    public TravellerController(MongoDBService mongoDBService)
    {
        _mongoDBService = mongoDBService;
    }

    // GET request for retrieving traveler details
    [Authorize(Roles = "backOffice,traveller")]
    [HttpGet]
    async public Task<IActionResult> Get([FromQuery] string? id)
    //Implementation for retrieving and returning traveler details
    {
        try
        {

            var claims = HttpContext.User.Claims;


            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            var role = claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);
            var val = await _mongoDBService.Traveller.FindByIdAsync((role.Value == "traveller" ? _id.Value : id) ?? "");

            if (val == null)
            {
                return NotFound(new ErrorResponse
                {
                    ErrorCode = 404,
                    Message = "Not Found",
                    Details = "Requested admin not Found"
                });
            }
            val.password = "";

            return Ok(new DataResponse<TravellerModel>
            {
                status = 200,
                data = val
            });
        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    // GET request for retrieving all travelers
    [Authorize(Roles = "backOffice,travelAgent")]
    [HttpGet("all")]
    async public Task<IActionResult> GetAll([FromQuery] bool active)
    // Implementation for retrieving and returning all travelers
    {
        try
        {
            List<TravellerModel> val = new List<TravellerModel>();
            if (active)
            {
                val = await _mongoDBService.Traveller.GetAllAsync();

            }
            else
            {

                val = await _mongoDBService.Traveller.GetActiveAsync();
            }

            foreach (var traveller in val)
            {
                traveller.password = "";
            }

            return Ok(new DataResponse<List<TravellerModel>>
            {
                status = 200, //Success 
                data = val
            });
        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found")); //Duplicate Error
        }

    }

    // POST request for adding a new traveler
    [HttpPost("add")]
    async public Task<IActionResult> Add([FromBody] TravellerModel traveller)
    // Implementation for adding a new traveler
    {

        try
        {

            traveller.active = true;
            traveller._isDeleted = false;
            traveller.HashPassword();
            await _mongoDBService.Traveller.CreateAsync(traveller);



            // TravellerBase _admin = new TravellerBase { username = traveller.username, role = traveller.role };

            traveller.password = "";

            return Ok(new DataResponse<TravellerModel>
            {
                status = 200,
                data = traveller
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }



    }

    // POST request for updating traveler details
    [Authorize(Roles = "traveller")]
    [HttpPost("update")]
    async public Task<IActionResult> Update([FromBody] TravellerBase _traveller)
    // Implementation for updating traveler details
    {
        try
        {

            var claims = HttpContext.User.Claims;
            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            var traveller = new TravellerModel
            {
                fullName = _traveller.fullName,
                email = _traveller.email,
                password = "",
                NIC = _traveller.NIC,
                active = _traveller.active,
                _isDeleted = false
            };

            var _res = await _mongoDBService.Traveller.UpdateAsync(_id.Value ?? "", traveller);

            _res.password = "";

            if (_res == null)
            {

                return BadRequest(new DataResponse<string>
                {
                    status = 400,
                    data = "Update Unsuccessfull!"
                });
            }
            else
            {
                return Ok(new DataResponse<TravellerModel>
                {
                    status = 200,
                    data = _res
                });

            }


        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }

    // POST request for deactivating a traveler
    [Authorize(Roles = "traveller")]
    [HttpPost("deactivate")]
    // POST request for deactivating a traveler
    async public Task<IActionResult> Deactivate()
    {
        try
        {
            var claims = HttpContext.User.Claims;
            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            await _mongoDBService.Traveller.DeActivateAsync(_id.Value);
            return Ok(new DataResponse<string>
            {
                status = 200,
                data = "Succesfully Deactivated"
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

    // DELETE request for deleting a traveler
    [Authorize(Roles = "backOffice")]
    [HttpDelete("delete")]
    // Implementation for deleting a traveler
    async public Task<IActionResult> Delete([FromQuery] string id)
    {
        try
        {

            await _mongoDBService.Traveller.DeleteAsync(id);
            return Ok(new DataResponse<string>
            {
                status = 200,
                data = "Succesfully Deleted"
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

    // POST request for activating/deactivating a traveler
    [Authorize(Roles = "backOffice")]
    [HttpPost("activate")]
    // Implementation for activating/deactivating a traveler
    async public Task<IActionResult> Activate([FromQuery] string id, [FromQuery] bool active)
    {
        try
        {

            await _mongoDBService.Traveller.UpdateActiveAsync(id, active);
            return Ok(new DataResponse<string>
            {
                status = 200,
                data = "Succesfully Updated!"
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

    // POST request for changing a traveler's password
    [Authorize(Roles = "traveller")]
    [HttpPost("change-pw")]
    // Implementation for changing a traveler's password
    async public Task<IActionResult> ChangePassword([FromBody] ResChangePassword res)
    {
        try
        {
            var claims = HttpContext.User.Claims;
            if (claims.IsNullOrEmpty())
            {
                return Unauthorized(new ErrorResponse
                {
                    ErrorCode = 401,
                    Message = "Invalid token",
                    Details = "No relevent user claims found"
                });
            }

            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            var newPassword = res.newPassword;
            var currentPassword = res.currentPassword;
            var _admin = await _mongoDBService.Admin.FindByIdAsync(_id == null ? "" : _id.Value);

            if (newPassword == null)
            {
                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Invalid new Password",
                    Details = "New Password should have minimum 6 characters"
                });
            }



            var _traveller = await _mongoDBService.Traveller.FindByIdAsync(_id == null ? "" : _id.Value);

            if (_traveller == null)
            {
                return NotFound(new ErrorResponse
                {
                    ErrorCode = 401,
                    Message = "Not Found",
                    Details = "Requested id Not Found"
                });
            }

            if (_traveller.VerifyPassword(currentPassword ?? ""))
            {
                _traveller.password = newPassword;
                _traveller.HashPassword();

                await _mongoDBService.Traveller.UpdatePasswordAsync(_id == null ? "" : _id.Value, _traveller.password);

                return Ok(new DataResponse<string>
                {
                    status = 200,
                    data = "Succesfully Updated"
                });
            }
            else
            {
                return Unauthorized(new ErrorResponse
                {
                    ErrorCode = 401,
                    Message = "Unauthorized request",
                    Details = "Old Password is not Correct"
                });
            }
        }
        catch (System.Exception ex)
        {
            System.Console.WriteLine(ex);
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }



}
