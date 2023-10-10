// This controller handels HTTP Requests For admin management Function
using Microsoft.AspNetCore.Mvc;
using System;
using web_service.Services;
using web_service.Models;
using web_service.Utility;
using web_service.Helper;
using web_service.Formatter;
using Microsoft.AspNetCore.Authorization;

namespace web_service.Controllers;

[ApiController]
[Route("api/[controller]")]
public class AdminController : ControllerBase
{

    private readonly MongoDBService _mongoDBService;

    public AdminController(MongoDBService mongoDBService)
    {
        _mongoDBService = mongoDBService;
    }

    //admin user controller
    [Authorize(Roles = "backOffice,travelAgent")]
    [HttpGet]
    async public Task<IActionResult> Get([FromQuery] string? id)
    {
        try
        {
            var claims = HttpContext.User.Claims;

            var _id = claims.FirstOrDefault(c => c.Type == "uid");
            // System.Console.WriteLine(_id.Value);

            var val = await _mongoDBService.Admin.FindByIdAsync(id ?? _id.Value);

            if (val == null)
            {
                return NotFound(new ErrorResponse
                {
                    ErrorCode = 404,
                    Message = "Not Found",
                    Details = "Requested admin not Found" //error handling
                });
            }
            val.password = "";

            return Ok(new DataResponse<AdminModel>
            {
                status = 200,
                data = val
            });
        }
        catch (System.Exception ex)
        {

            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found")); //validation
        }
    }

    //access permission controller for backoffice
    [Authorize(Roles = "backOffice")]
    [HttpGet("all")]
    async public Task<IActionResult> GetAll([FromQuery] bool active)
    {
        try
        {
            List<AdminModel> val = new List<AdminModel>();
            if (active)
            {
                val = await _mongoDBService.Admin.GetAllAsync();

            }
            else
            {

                val = await _mongoDBService.Admin.GetActiveAsync();
            }



            return Ok(new DataResponse<List<AdminModel>>
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
    //access permission controller for travel agent
    [Authorize(Roles = "traveller")]
    [HttpGet("agents")]
    async public Task<IActionResult> GetAllAgents()
    {
        try
        {
            List<AdminModel> val = new List<AdminModel>();


            val = await _mongoDBService.Admin.getAgents();

            foreach (var agent in val)
            {
                agent.password = "";
            }



            return Ok(new DataResponse<List<AdminModel>>
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

    [Authorize(Roles = "backOffice")]
    [HttpPost("add")]
    async public Task<IActionResult> Add([FromBody] AdminModel admin)
    {

        try
        {

            if (admin.password == null)
            {
                return BadRequest(new ErrorResponse
                {
                    ErrorCode = 400,
                    Message = "Invalid new Password",
                    Details = "New Password should have minimum 6 characters"
                });
            }

            admin.active = true;
            admin._isDeleted = false;
            admin.HashPassword();
            await _mongoDBService.Admin.CreateAsync(admin);



            AdminBase _admin = new AdminBase { username = admin.username, role = admin.role };

            return Ok(new DataResponse<AdminBase>
            {
                status = 200,
                data = _admin
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }

    [Authorize(Roles = "backOffice")]
    [HttpPost("update")]
    async public Task<IActionResult> Update([FromBody] AdminBase _admin)
    {
        try
        {

            var admin = new AdminModel
            {
                username = _admin.username ?? "",
                role = _admin.role,
            };

            if (_admin.password != null) admin.HashPassword();
            await _mongoDBService.Admin.UpdateAsync(_admin._id ?? "", admin);

            _admin.password = "";

            return Ok(new DataResponse<AdminBase>
            {
                status = 200,
                data = _admin
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }


    }

    [Authorize(Roles = "backOffice")]
    [HttpPost("activate")]
    // public IActionResult Update([FromBody] AdminModel model)
    async public Task<IActionResult> Activate([FromQuery] string id, [FromQuery] bool active)
    {
        try
        {

            await _mongoDBService.Admin.ActivateAsync(id, active);
            return Ok(new DataResponse<string>
            {
                status = 200,
                data = active ? "Succesfully activated" : "Succesfully deactivated"
            });
        }
        catch (System.Exception ex)
        {
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }
    }


    [Authorize(Roles = "backOffice")]
    [HttpDelete("delete")]
    async public Task<IActionResult> Delete([FromQuery] string id)
    {
        try
        {

            await _mongoDBService.Admin.DeleteAsync(id);
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

//password change for admin users
    [Authorize(Roles = "backOffice,travelAgent")]
    [HttpPost("change-pw")]
    async public Task<IActionResult> ChangePassword([FromBody] ResChangePassword res)
    {
        try
        {
            var claims = HttpContext.User.Claims;
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

            if (_admin.VerifyPassword(currentPassword ?? ""))
            {
                _admin.password = newPassword;
                _admin.HashPassword();

                await _mongoDBService.Admin.UpdateAsync(_id == null ? "" : _id.Value, _admin);

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
            ErrorFormatter _error = new ErrorFormatter(ex);
            return StatusCode(500, _error.Get("Duplicate Field Found"));
        }

    }



}
