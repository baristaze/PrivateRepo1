//
//  StarView.m
//  StarRating
//
//  Created by David Linsin on 1/4/11.
//

#import "DLStarView.h"


@implementation DLStarView

#pragma mark -
#pragma mark Initialization

- (id)initWithDefault:(UIImage*)star 
             selected:(UIImage*)selectedStar 
          highlighted:(UIImage*)highlightedStar 
             position:(int)index 
{
	self = [super initWithFrame:CGRectMake((star.size.width*index), 0, star.size.width, star.size.height+kEdgeInsetBottom)];
	if (self) {
		[self setImage:star forState:UIControlStateNormal];
		[self setImage:selectedStar forState:UIControlStateSelected];
		[self setImage:highlightedStar forState:UIControlStateHighlighted];
		[self setTag:index];
		[self setImageEdgeInsets:UIEdgeInsetsMake(0, 0, kEdgeInsetBottom, 0)];
		[self setBackgroundColor:[UIColor clearColor]];
	}
	return self;
}

#pragma mark -
#pragma mark UIView methods

- (UIView*)hitTest:(CGPoint)point withEvent:(UIEvent *)event {
	return self.superview;
}

#pragma mark -
#pragma mark Layouting

- (void)centerIn:(CGRect)_frame with:(int)numberOfStars {
	CGSize size = self.frame.size;
	
	float height = self.frame.size.height;
	float frameHeight = _frame.size.height;
	float newY = (frameHeight - height) / 2;
	
    self.frame = CGRectMake((size.width*self.tag), newY, size.width, size.height);	
}

@end
