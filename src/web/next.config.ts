import type { NextConfig } from "next";

const nextConfig: NextConfig = { 
// redirection to springboot java backend API
  /* config options here */

   async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:8080/api/:path*"
      }
    ];
  },
};


export default nextConfig;
