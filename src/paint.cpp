#include <opencv2/opencv.hpp>
#include <opencv2/core.hpp>
#include <opencv2/highgui.hpp>
#include <string>
using namespace cv;
using namespace std;

int height=0;
int width=0;
int **red;
int **green;
int **blue;
int bin[256]={0};
Mat image;
const double p=0.2989;
const double q=0.5870;
const double t=0.1140;

void init ()
{
    height=image.rows;
    width=image.cols;

    red=new int* [height];
    green=new int* [height];
    blue=new int* [height];

    for(int i=0;i<height;i++)
    {
        red[i]=new int [width];
        green[i]=new int [width];
        blue[i]=new int [width];        
    }
}
//====================================================================
int GetCountBin(Mat& map,int row,int col,int bin [],int dest )
{
    for(int i=-1;i<=1;i++)
        for(int j=-1;j<=1;j++)
            {   
                bin[(unsigned int)image.at<Vec3b>(row+i,col+j)[dest]]++;
            }
    int max=-999999;
    int index=-1;
    for(int i=0;i<256;i++)
        {
            if(bin[i]>max)
                {
                    max=bin[i];
                    index=i;
                }
        }
    for(int i=-1;i<=1;i++)
        for(int j=-1;j<=1;j++)
            {
                bin[(unsigned int)image.at<Vec3b>(row+i,col+j)[dest]]=0;
            }
    return index;
}

void paint(Mat& image,int ** buffer,int dest)
{
    for(int i=0;i<height;i++)
        {
            buffer[i][0]=(unsigned int)image.at<Vec3b>(i,0)[dest];
            buffer[i][width-1]=(unsigned int)image.at<Vec3b>(i,width-1)[dest];
        }
    for(int i=0;i<width;i++)
        {
            buffer[0][i]=(unsigned int)image.at<Vec3b>(0,i)[dest];
            buffer[height-1][i]=(unsigned int)image.at<Vec3b>(height-1,i)[dest];
        }

    for(int i=1;i<height-1;i++)
        for(int j=1;j<width-1;j++)
            {   
                buffer[i][j]=GetCountBin(image,i,j,bin,dest);
            } 
    for(int i=0;i<height;i++)
        for(int j=0;j<width;j++)
            {
                image.at<Vec3b>(i,j)[dest]=buffer[i][j];
            }

}
//==================================================================
int GetCountBin(Mat& map,int row,int col,int bin [],int dest,int flag)
{
    for(int i=-1;i<=1;i++)
        for(int j=-1;j<=1;j++)
            {   
                int g=t*(unsigned int)map.at<Vec3b>(row+i,col+j)[0]+q*(unsigned int)map.at<Vec3b>(row+i,col+j)[1]+p*(unsigned int)map.at<Vec3b>(row+i,col+j)[2];
                //if(g==120)
                //    cout<<" get g"<<g<<endl;
                bin[g]++;
            }
    int max=-999999;
    int index=-1;
    for(int i=0;i<256;i++)
        {
            if(bin[i]>max)
                {
                    max=bin[i];
                    index=i;
                }
        }
    for(int i=0;i<256;i++)
                bin[i]=0;
    return index;
}

void paint(Mat& image,Mat& buffer,int dest,int flag)
{
    for(int i=0;i<height;i++)
        {
            buffer.at<Vec3b>(i,0)[0]=(unsigned int)image.at<Vec3b>(i,0)[0];
            buffer.at<Vec3b>(i,width-1)[0]=(unsigned int)image.at<Vec3b>(i,width-1)[0];
            buffer.at<Vec3b>(i,0)[1]=(unsigned int)image.at<Vec3b>(i,0)[1];
            buffer.at<Vec3b>(i,width-1)[1]=(unsigned int)image.at<Vec3b>(i,width-1)[1];
            buffer.at<Vec3b>(i,0)[2]=(unsigned int)image.at<Vec3b>(i,0)[2];
            buffer.at<Vec3b>(i,width-1)[2]=(unsigned int)image.at<Vec3b>(i,width-1)[2];
        }
    for(int i=0;i<width;i++)
        {
            buffer.at<Vec3b>(0,i)[0]=(unsigned int)image.at<Vec3b>(0,i)[0];
            buffer.at<Vec3b>(height-1,i)[0]=(unsigned int)image.at<Vec3b>(height-1,i)[0];
            buffer.at<Vec3b>(0,i)[1]=(unsigned int)image.at<Vec3b>(0,i)[1];
            buffer.at<Vec3b>(height-1,i)[1]=(unsigned int)image.at<Vec3b>(height-1,i)[1];
            buffer.at<Vec3b>(0,i)[2]=(unsigned int)image.at<Vec3b>(0,i)[2];
            buffer.at<Vec3b>(height-1,i)[2]=(unsigned int)image.at<Vec3b>(height-1,i)[2];
        }

    for(int i=1;i<height-1;i++)
        for(int j=1;j<width-1;j++)
            {   
                int index=GetCountBin(image,i,j,bin,dest,1);
                int rsum=0;
                int gsum=0;
                int bsum=0;
                int counter=0;
                //cout<<"1 called"<<endl;
                for(int m=-1;m<=1;m++)
                    for(int n=-1;n<=1;n++)
                    {   
                        int g=t*(unsigned int)image.at<Vec3b>(i+m,j+n)[0]+q*(unsigned int)image.at<Vec3b>(i+m,j+n)[1]+p*(unsigned int)image.at<Vec3b>(i+m,j+n)[2];
                        if(g==index)
                        {
                            rsum+=buffer.at<Vec3b>(i+m,j+n)[2];
                            gsum+=buffer.at<Vec3b>(i+m,j+n)[1];
                            bsum+=buffer.at<Vec3b>(i+m,j+n)[0];
                            counter++;
                        }
                    }
                buffer.at<Vec3b>(i,j)[2]=rsum/counter;
                buffer.at<Vec3b>(i,j)[1]=gsum/counter;
                buffer.at<Vec3b>(i,j)[0]=bsum/counter;
            } 
    image=buffer.clone();

}


//================================================================
int main(int argc, char *argv[] )
{
    if (argc < 3)
    {
        cout << "usage: " << argv[0] << " <input file> <output file>" <<endl;
        return 0;
    }
    image = imread(argv[1]);
    int channels = image.channels();
    init();
    if (channels == 1)
    {
        paint(image, red, 0);
        imwrite(argv[2], image);
        return 0;
    }
    else if (channels == 3)
    {
        Mat oriImage; 
        oriImage = imread(argv[1]);
        paint(oriImage, image, 0, 1);
        imwrite(argv[2], oriImage);
        return 0;
    }
    else
        cout<<"invaild channels(must be 1 or 3)"<<endl;
    return 0;
}
