import { query } from '../config/database';
import bcrypt from 'bcrypt';

export interface User {
  id: string;
  name: string;
  email: string;
}

export class AuthRepository {
  async createUser(name: string, email: string, password: string): Promise<User> {
    const passwordHash = await bcrypt.hash(password, 10);

    const sql = `
      INSERT INTO users (name, email, password_hash)
      VALUES ($1, $2, $3)
      RETURNING id, name, email
    `;

    const result = await query(sql, [name, email, passwordHash]);
    return result.rows[0];
  }

  async getUserByEmail(email: string): Promise<User & { password_hash: string } | null> {
    const sql = `
      SELECT id, name, email, password_hash
      FROM users
      WHERE email = $1
    `;

    const result = await query(sql, [email]);
    if (result.rows.length === 0) return null;

    return result.rows[0];
  }

  async getUserById(id: string): Promise<User | null> {
    const sql = `
      SELECT id, name, email
      FROM users
      WHERE id = $1
    `;

    const result = await query(sql, [id]);
    if (result.rows.length === 0) return null;

    return result.rows[0];
  }

  async verifyPassword(password: string, hash: string): Promise<boolean> {
    return bcrypt.compare(password, hash);
  }
}
