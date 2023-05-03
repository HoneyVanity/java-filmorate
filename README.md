# java-filmorate
## Film rating application.

### *ER-Diagram*
![](/Database ER diagram.png)

### *SQL-Requests examples:*

1. **Count likes for the film with id = 1**

SELECT
COUNT(user_id)
FROM likes
WHERE film_id = 1; 

2. **Get names of all confirmed friends for user with id = 2**

SELECT name
FROM users
WHERE user_id IN (
SELECT friend_id
FROM friendship
WHERE user_id = 2
AND confirmed = true
UNION
SELECT user_id
FROM friendship
WHERE friend_id = 2
AND confirmed = true;