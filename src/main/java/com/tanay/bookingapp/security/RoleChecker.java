package com.tanay.bookingapp.security;

import jakarta.servlet.http.HttpServletRequest;

public class RoleChecker {

public static boolean hasRole(HttpServletRequest request, String requiredRole) {

Object role = request.getAttribute("role");

if(role == null) {
return false;
}

return role.toString().equalsIgnoreCase(requiredRole);
}
}
