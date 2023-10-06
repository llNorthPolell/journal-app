import NextAuth from 'next-auth'
import { JWT } from "next-auth/jwt"

declare module 'next-auth' {
  interface Session {
    user: User
    
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    access_token?: Account.access_token;
    id_token? : Account.id_token;
  }
}