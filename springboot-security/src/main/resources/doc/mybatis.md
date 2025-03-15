resultMap Api
id   reference resultMap
type javaType

result column sql_column  property javaType

<resultMap id="" type="">
        <id column="director_id" property="directorId"/>
        <result column="director_name" property="directorName"/>
</resultMap>


association

 <resultMap id="" type="">
        <id column="movie_id" property="movieId"/>
        <result column="movie_name" property="movieName"/>
        <association property="director" column="director_id" javaType="Director" resultMap="directorResult"/>
</resultMap>


sql

<sql id="basicColumn">
    movie_id,movie_name
</sql>

insert

<insert id="insertMovie" parameterType="Movie" useGeneratedKeys="true" keyProperty="movieId">
insert into movies(
<if test="movieName != null and movieName != ''">movie_name</if>
)values(
<if test="movieName != null and movieName != ''">#{movieName}</if>
)

</insert>

    <select id="selectAllMovie" resultMap="movieResult">
        select m.movie_id, m.movie_name,
        d.director_id, d.director_name
        from movies m
        left join director d on m.movie_director_id = d.director_id
    </select>