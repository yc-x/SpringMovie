package dev.example.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Review createReview(String reviewBody, String imdbId){
        Review review = reviewRepository.insert(new Review(reviewBody));

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))     // find the corresponding movie where to add review
                .apply(new Update().push("reviewIds").value(review))   //update a new review
                .first();   //Update the first document in the collection. Ref: https://docs.spring.io/spring-data/mongodb/docs/current/api/org/springframework/data/mongodb/core/ExecutableUpdateOperation.TerminatingUpdate.html#first()
        return review;
    }
}
